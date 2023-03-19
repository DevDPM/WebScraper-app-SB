package com.webcrawler.webcrawlerapp.io.searchEngines;

import com.webcrawler.webcrawlerapp.domain.Keyword;

public class BingSearchEngine implements SearchEngine{
    @Override
    public String getSearchUrl(Keyword keyword) {
        if (keyword.getNumberOfPages() < 0 || keyword.getKeyword() == null || keyword.getKeyword().equals(""))
            return null;

        String searchUrl = "https://www.bing.nl" +
                "/search?q=" +
                reformatKeyword(keyword.getKeyword()) +
                "&count=" +
                keyword.getNumberOfPages();
        return searchUrl;
    }

    @Override
    public String reformatKeyword(String keyword) {
        return keyword.replace(" ", "+");
    }
}
