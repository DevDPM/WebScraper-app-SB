package com.webcrawler.webcrawlerapp.utils.searchEngines;

import java.util.List;
import java.util.Set;

public interface SearchEngine {
    String getSearchUrl(String keyword, int numberOfHttpPages);
    Set<String> subtractHttpDomain(List<String> urls);

}
