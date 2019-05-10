package ru.ifmo.rain.istomin.crawler;

import info.kgeorgiy.java.advanced.crawler.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class WebCrawler implements Crawler {
	private ExecutorService extractors;
	private ExecutorService downloaders;
	private Downloader downloader;
	private int perHost;

	public WebCrawler(Downloader downloader, int downloaders, int extractors, int perHost) {
		this.downloader = downloader;
        this.downloaders = new ThreadPoolExecutor(downloaders, downloaders, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        this.extractors = new ThreadPoolExecutor(extractors, extractors, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
		this.perHost = perHost;
	}

	@Override
	public Result download(String url, int depth) {
		Map<String, IOException> errors = new HashMap<>();
		List<String> success = new ArrayList<>();
		Set<String> allUrls = new HashSet<>();
		allUrls.add(url);
		ArrayList<String> currentLevel = new ArrayList<>();
		ArrayList<String> nextLevel = new ArrayList<>();
		currentLevel.add(url);
		while (!(currentLevel.isEmpty()||depth<=0)) {
		    ArrayList<String> subCurrent = new ArrayList<>(Collections.singletonList("1"));
		    while (!subCurrent.isEmpty() || !currentLevel.isEmpty()) {
                subCurrent.clear();
                ArrayList<Future> futures = new ArrayList<>();
                HashMap<String, Integer> perHostConnections = new HashMap<>();
                for (int i = 0; i < currentLevel.size(); i++) {
                    try {
                        //todo: add support of per host limits
                        String host = URLUtils.getHost(currentLevel.get(i));
                        if (perHost>0 && perHostConnections.containsKey(host) && perHostConnections.get(host) >= perHost) {
                            subCurrent.add(currentLevel.get(i));
                            currentLevel.remove(i);
                            i--;
                        } else {
                            perHostConnections.put(host, perHostConnections.containsKey(host)?(perHostConnections.get(host)+1):1);
                            futures.add(downloaders.submit(new DownloadTask(downloader, currentLevel.get(i))));
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }

                ArrayList<Future> extractorsFuture = new ArrayList();
                for (int i = 0; i < currentLevel.size(); i++) {
                    try {
                        while (!futures.get(i).isDone()) Thread.sleep(1);
                        Document doc = (Document) futures.get(i).get();
                        extractorsFuture.add(extractors.submit(new ExtractorTask(doc)));
                    } catch (Exception e) {
                        if (e.getCause() instanceof IOException) {
                            success.remove(currentLevel.get(i));
                            errors.put(currentLevel.get(i), (IOException) e.getCause());
                            currentLevel.remove(i);
                            futures.remove(i);
                            i--;
                        }
                    }
                }

                for (int i = 0; i < currentLevel.size(); i++) {
                    try {
                        Set<String> parsed = new HashSet<>((List<String>) extractorsFuture.get(i).get());
//                        Set<String> parsed = new HashSet<>((List<String>) futures.get(i).get());
                        //ForkJoinPool forkJoinPool = new ForkJoinPool(extractors);
                        parsed = parsed.stream().filter(it -> !allUrls.contains(it)).collect(Collectors.toSet());

                        Set<String> slashed = parsed.stream()
                                .filter(it -> allUrls.contains(it + "/") || it.endsWith("/") && allUrls.contains(it.substring(0, it.length() - 1)))
                                .collect(Collectors.toSet());
                        allUrls.addAll(parsed);
                        parsed.removeAll(slashed);

                        nextLevel.addAll(parsed);
                        success.add(currentLevel.get(i));
                        slashed.forEach(it -> {
                            if (success.contains(it + "/")) {
                                success.add(it);
                            } else if (it.endsWith("/") && success.contains(it.substring(0, it.length() - 1))) {
                                success.add(it);
                            } else if (errors.keySet().contains(it + "/")) {
                                errors.put(it, errors.get(it + "/"));
                            } else if (it.endsWith("/") && errors.keySet().contains(it.substring(0, it.length() - 1))) {
                                errors.put(it, errors.get(it.substring(0, it.length() - 1)));
                            }
                        });
                    } catch (Exception e) {
                        if (e.getCause() instanceof IOException) {
                            success.remove(currentLevel.get(i));
                            errors.put(currentLevel.get(i), (IOException) e.getCause());
                        }
                    }
                }
                currentLevel = (ArrayList<String>) subCurrent.clone();
                subCurrent.clear();
            }
			currentLevel = (ArrayList<String>) nextLevel.clone();
			nextLevel.clear();
			depth--;
		}
		downloaders.shutdown();
		extractors.shutdown();
		return new Result(success, errors);
	}

	@Override
	public void close() {
        downloaders.shutdown();
        extractors.shutdown();
	}

	public static void main (String... args) throws IOException {
		int depth = args.length>1? Integer.parseInt(args[1]) :0;
		int downloads = args.length>2? Integer.parseInt(args[2]):10;
		int extractors = args.length>3? Integer.parseInt(args[3]):10;
		int perHost = args.length>4? Integer.parseInt(args[4]):0;

		WebCrawler crawler = new WebCrawler(new CachingDownloader(), extractors, downloads, perHost);
		Result res = crawler.download(args[0], depth);
		res.getDownloaded().forEach(System.out::println);
        System.out.println("err");
		res.getErrors().keySet().forEach(System.out::println);
	}
}
