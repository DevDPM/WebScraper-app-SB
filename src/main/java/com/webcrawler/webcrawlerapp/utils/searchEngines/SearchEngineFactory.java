package com.webcrawler.webcrawlerapp.utils.searchEngines;

import com.webcrawler.webcrawlerapp.utils.WebSearchScraping;

public class SearchEngineFactory {
    public SearchEngine getSearchEngine(WebSearchScraping.Engine engine) {
        if (engine == null)
            return null;

        return switch (engine) {
            case GOOGLE -> new GoogleSearchEngine();
            case BING -> new BingSearchEngine();
            default -> throw new IllegalArgumentException("Unknown engine");
        };
    }
}
