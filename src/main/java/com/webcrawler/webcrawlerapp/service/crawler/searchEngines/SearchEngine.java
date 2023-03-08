package com.webcrawler.webcrawlerapp.service.crawler.searchEngines;

import java.util.List;
import java.util.Set;

public interface SearchEngine {
    String getSearchUrl(String keyword, int numberOfHttpPages);
    Set<String> subtractHttpDomain(List<String> urls);

}
