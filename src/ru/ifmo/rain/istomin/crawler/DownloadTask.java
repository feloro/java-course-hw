package ru.ifmo.rain.istomin.crawler;

import info.kgeorgiy.java.advanced.crawler.Downloader;

import java.io.IOException;
import java.util.concurrent.Callable;

public class DownloadTask implements Callable {
    Downloader downloader;
    String url;

    @Override
    public Object call() throws IOException {
        return downloader.download(url);
    }

    public DownloadTask(Downloader downloader, String url) {
        this.downloader = downloader;
        this.url = url;
    }
}
