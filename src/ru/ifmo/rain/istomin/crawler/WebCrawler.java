package ru.ifmo.rain.istomin.crawler;

import info.kgeorgiy.java.advanced.crawler.CachingDownloader;
import info.kgeorgiy.java.advanced.crawler.Crawler;
import info.kgeorgiy.java.advanced.crawler.Document;
import info.kgeorgiy.java.advanced.crawler.Downloader;
import info.kgeorgiy.java.advanced.crawler.Result;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebCrawler implements Crawler {
	private ExecutorService extractors;
	private ExecutorService downloaders;
	private Downloader downloader;
	private int perHost;

	public WebCrawler(Downloader downloader, int extractors, int downloaders, int perHost) {
		this.downloader = downloader;
		this.extractors = Executors.newFixedThreadPool(extractors);
		this.downloaders = Executors.newFixedThreadPool(downloaders);
		this.perHost = perHost;
	}

	@Override
	public Result download(String url, int depth) {
		Map<String, IOException> errors = new HashMap<>();
		List<String> success = new ArrayList<>();
		Set<String> allUrls = new HashSet<>();
		allUrls.add(url);
		ArrayDeque<String> currentLevel = new ArrayDeque<>();
		ArrayDeque<String> nextLevel = new ArrayDeque<>();
		currentLevel.add(url);
		while (!(currentLevel.isEmpty()||depth<=0)) {
			while (!currentLevel.isEmpty()) {
				String currentUrl = currentLevel.poll();
				try {
					Document siteDoc = downloader.download(currentUrl);
					siteDoc.extractLinks().stream().filter(it -> !allUrls.contains(it)).forEach(it -> {
						nextLevel.push(it);
						allUrls.add(it);
					});
					success.add(currentUrl);
				} catch (IOException e) {
					errors.put(currentUrl,e);
				}
			}
			currentLevel = nextLevel.clone();
			nextLevel.clear();
			depth--;
		}
		return new Result(success, errors);
	}

	@Override
	public void close() {

	}

	public static void main (String... args) throws IOException {
		int depth = args.length>1? Integer.parseInt(args[1]) :0;
		int downloads = args.length>2? Integer.parseInt(args[2]):1;
		int extractors = args.length>3? Integer.parseInt(args[3]):1;
		int perHost = args.length>4? Integer.parseInt(args[4]):1;

		WebCrawler crawler = new WebCrawler(new CachingDownloader(), extractors, downloads, perHost);
		crawler.download(args[0], depth).getDownloaded().forEach(System.out::println);
	}
}
