package com.webcrawler.webcrawlerapp.io.searchEngines;

import com.webcrawler.webcrawlerapp.domain.Keyword;

public class YahooSearchEngine implements SearchEngine{
    @Override
    public String getSearchUrl(Keyword keyword) {
        if (keyword.getNumberOfPages() < 0 || keyword.getKeyword() == null || keyword.getKeyword().equals(""))
            return null;

        String searchUrl = "https://search.yahoo.com" +
                "/search?p=" +
                reformatKeyword(keyword.getKeyword()) +
                "&n=" +
                keyword.getNumberOfPages();
        return searchUrl;
    }

    @Override
    public String reformatKeyword(String keyword) {
        return keyword.replace(" ", "+");
    }
}
