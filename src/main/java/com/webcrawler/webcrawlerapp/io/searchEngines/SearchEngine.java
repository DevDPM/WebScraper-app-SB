package com.webcrawler.webcrawlerapp.io.searchEngines;

import com.webcrawler.webcrawlerapp.domain.Keyword;
import com.webcrawler.webcrawlerapp.domain.KeywordDTO;

public interface SearchEngine {
    String getSearchUrl(Keyword keyword);
    String reformatKeyword(String reformatKeyword);

}
