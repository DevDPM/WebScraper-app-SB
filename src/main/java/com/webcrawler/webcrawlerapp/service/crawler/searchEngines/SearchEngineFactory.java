package com.webcrawler.webcrawlerapp.service.crawler.searchEngines;

import com.webcrawler.webcrawlerapp.service.crawler.CrawlSearchEngineServiceImpl;


public class SearchEngineFactory {
    public SearchEngine getSearchEngine(CrawlSearchEngineServiceImpl.Engine engine) {
        if (engine == null)
            return null;

        return switch (engine) {
            case GOOGLE -> new GoogleSearchEngine();
            case BING -> new BingSearchEngine();
            default -> throw new IllegalArgumentException("Unknown engine");
        };
    }
}
