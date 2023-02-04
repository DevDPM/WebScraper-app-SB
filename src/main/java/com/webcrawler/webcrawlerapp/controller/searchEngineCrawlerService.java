package com.webcrawler.webcrawlerapp.controller;

import com.webcrawler.webcrawlerapp.domain.inspectedUrl;

import java.util.Set;

public interface searchEngineCrawlerService {

    Set<inspectedUrl> getUrlsBySearchEngine(String searchEngine);
}
