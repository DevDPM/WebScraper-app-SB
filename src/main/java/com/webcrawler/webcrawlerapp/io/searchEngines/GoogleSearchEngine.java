package com.webcrawler.webcrawlerapp.io.searchEngines;

import com.webcrawler.webcrawlerapp.domain.Keyword;

public class GoogleSearchEngine implements SearchEngine {


    @Override
    public String getSearchUrl(Keyword keyword) {
        if (keyword.getNumberOfPages() < 0 || keyword.getKeyword() == null || keyword.getKeyword().equals(""))
            return null;

        String searchUrl = "https://www.google.nl" +
                "/search?q=" +
                reformatKeyword(keyword.getKeyword()) +
                "&num=" +
                keyword.getNumberOfPages();
        return searchUrl;
    }


    @Override
    public String reformatKeyword(String keyword) {
        return keyword.replace(" ", "+");
    }
}
