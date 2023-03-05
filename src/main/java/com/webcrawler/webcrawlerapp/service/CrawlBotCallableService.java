package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.controller.URL_PATH;
import com.webcrawler.webcrawlerapp.domain.Keyword;
import com.webcrawler.webcrawlerapp.domain.KeywordProgression;
import com.webcrawler.webcrawlerapp.domain.Url;
import com.webcrawler.webcrawlerapp.utils.HttpSearchScraping;
import com.webcrawler.webcrawlerapp.utils.WebSearchScraping;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Service
public class CrawlBotCallableService implements Callable<ResponseEntity> {

    private final KeywordProgressionService keywordProgressionService;
    private final KeywordService keywordService;
    private boolean googleSearch;
    private boolean bingSearch;
    private int findNumberOfPages;
    private Keyword keyword;

    public CrawlBotCallableService(KeywordProgressionService keywordProgressionService, KeywordService keywordService) {
        this.keywordProgressionService = keywordProgressionService;
        this.keywordService = keywordService;
    }

    @Override
    public ResponseEntity call() throws Exception {

        Keyword newKeyword = new Keyword();
        newKeyword.setKeyword(keyword.getKeyword());

        KeywordProgression newKeywordProgression = new KeywordProgression();
        newKeywordProgression.setKeyword(keyword.getKeyword());
        newKeywordProgression.setAdditionalInfo("Crawl search engines..");
        newKeywordProgression.setPercentageCompleted("0%");
        newKeywordProgression.setEstimatedTime("-");
        keywordProgressionService.addKeywordProgress(newKeywordProgression);

        WebSearchScraping webSearchScraping = new WebSearchScraping(keywordProgressionService);
        webSearchScraping.setKeyword(newKeyword.getKeyword());
        webSearchScraping.setGoogleSearch(googleSearch);
        webSearchScraping.setBingSearch(bingSearch);
        webSearchScraping.setFindNumberOfPages(findNumberOfPages);
        webSearchScraping.setKeywordProgression(newKeywordProgression);
        webSearchScraping.start();

        Set<String> urlSet = webSearchScraping.getUrlList();
        HttpSearchScraping httpSearchScraping = new HttpSearchScraping(keywordProgressionService);
        httpSearchScraping.setKeywordProgression(newKeywordProgression);
        httpSearchScraping.setUrls(urlSet);
        httpSearchScraping.setFindEmail(true);
        httpSearchScraping.setFindTelephone(true);
        List<Url> urlResults = httpSearchScraping.start();

        urlResults = urlResults.stream().map(e -> {
            e.setKeyword(newKeyword);
            return e;
        }).collect(Collectors.toList());

        keywordProgressionService.DeleteKeywordProgress(newKeywordProgression);

        newKeyword.getUrls().addAll(urlResults);
        newKeyword.setNumberOfPages(urlResults.size());
        Keyword savedKeyword = keywordService.saveNewKeyword(newKeyword);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", URL_PATH.API + savedKeyword.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    public void setGoogleSearch(boolean googleSearch) {
        this.googleSearch = googleSearch;
    }

    public void setBingSearch(boolean bingSearch) {
        this.bingSearch = bingSearch;
    }

    public void setFindNumberOfPages(int findNumberOfPages) {
        System.out.println("set to :" + findNumberOfPages);
        this.findNumberOfPages = findNumberOfPages;
    }

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }
}
