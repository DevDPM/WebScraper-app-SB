package com.webcrawler.webcrawlerapp.service.crawler;

import com.webcrawler.webcrawlerapp.domain.KeywordProgression;

import java.util.Set;

public interface CrawlSearchEngineService {

    void setKeyword(String keyword);
    void setGoogleSearch(boolean setGoogle);
    void setBingSearch(boolean setBing);
    void start();
    String replaceSpaceByPlus(String keyword);
    boolean addFoundUrlsToUrlList(Set<String> foundUrls);
    void addSearchEngines();
    void setKeywordProgression(KeywordProgression keywordProgression);
    int getNumberOfHttpPages();
    void setFindNumberOfPages(int numberOfHttpPages);
    Set<String> getUrlList();
    boolean isGoogleSearch();
    boolean isBingSearch();
    String getKeyword();
}
