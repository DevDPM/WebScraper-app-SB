package com.webcrawler.webcrawlerapp.service.crawler;

import com.webcrawler.webcrawlerapp.domain.KeywordProgression;
import com.webcrawler.webcrawlerapp.domain.Url;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CrawlHttpService {

    boolean setUrls(Set<String> urls);
    List<Url> start();
    Map<String, Integer> getTelephones();
    Map<String, Integer> getEmails();
    void setFindTelephone(boolean findTelephone);
    boolean isFindTelephone();
    void setFindEmail(boolean findEmail);
    boolean isFindEmail();
    Set<String> getUrlsToHttpCrawl();
    void setUrlResultList(List<Url> urlResultList);
    List<Url> getUrlResultList();
    void setKeywordProgression(KeywordProgression keywordProgression);

}
