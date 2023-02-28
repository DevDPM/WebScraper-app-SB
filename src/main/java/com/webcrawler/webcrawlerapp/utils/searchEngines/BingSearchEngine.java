package com.webcrawler.webcrawlerapp.utils.searchEngines;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BingSearchEngine implements SearchEngine{
    @Override
    public String getSearchUrl(String keyword, int numberOfHttpPages) {
        String googleUrl = "https://www.bing.nl" +
                "/search?q=" +
                keyword +
                "&count=" +
                numberOfHttpPages;
        return googleUrl;
    }

    @Override
    public Set<String> subtractHttpDomain(List<String> urls) {
        String startHttp = "https://";
        String endHttp = "/";
        Set<String> httpDomainSet = urls.stream()
                .filter(e -> e.contains(startHttp))
                .map(e -> {
                    int startIndex = e.indexOf(startHttp);
                    int endIndex = e.indexOf(endHttp, startIndex + startHttp.length());

                    String newUrl;
                    if (endIndex < 0 || endIndex < startIndex) {
                        newUrl = e.substring(startIndex);
                    } else {
                        newUrl = e.substring(startIndex, endIndex);
                    }

                    return newUrl;
                })
                .collect(Collectors.toSet());
        return httpDomainSet;
    }
}
