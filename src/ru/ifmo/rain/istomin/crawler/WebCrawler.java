package ru.ifmo.rain.istomin.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class WebCrawler implements Crawler {

	private Downloader downloader;
	private Thread[] extractors;
	private Thread[] downloaders;
	private int perHost;

	public WebCrawler(Downloader downloader, int extractors, int downloaders, int perHost) {
		this.downloader = downloader;
		this.extractors = new Thread[extractors];
		this.downloaders = new Thread[downloaders];
		this.perHost = perHost;
	}

	@Override
	public Result download(String url, int depth) {
		//ConcurrentLinkedDeque<String> resultUrls = new ArrayList<>();
		ConcurrentLinkedDeque<String> currentList = new ConcurrentLinkedDeque();
		currentList.add(url);
		while (depth!=0) {
//			try {
//				List<String> parsed = downloader.download(currentList.getFirst());
//				((ArrayList) resultUrls).addAll()
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
		return null;
	}

	@Override
	public void close() {

	}
}
