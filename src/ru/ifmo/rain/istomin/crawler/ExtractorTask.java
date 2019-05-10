package ru.ifmo.rain.istomin.crawler;

import info.kgeorgiy.java.advanced.crawler.Document;

import java.util.concurrent.Callable;

public class ExtractorTask implements Callable {

    Document document;

    @Override
    public Object call() throws Exception {
        return document.extractLinks();
    }

    public ExtractorTask(Document document) {
        this.document = document;
    }
}
