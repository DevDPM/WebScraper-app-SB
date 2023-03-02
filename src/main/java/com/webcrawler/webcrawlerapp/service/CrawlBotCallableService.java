package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.controller.URL_PATHS;
import com.webcrawler.webcrawlerapp.domain.Keyword;
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

    private final KeywordService keywordService;
    private boolean googleSearch;
    private boolean bingSearch;
    private int findNumberOfPages;
    private Keyword keyword;

    public CrawlBotCallableService(KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    @Override
    public ResponseEntity call() throws Exception {

        Keyword newKeyword = new Keyword();
        newKeyword.setKeyword(keyword.getKeyword());

        WebSearchScraping webSearchScraping = new WebSearchScraping();
        webSearchScraping.setKeyword(newKeyword.getKeyword());
        webSearchScraping.setFindNumberOfPages(10);
        webSearchScraping.setGoogleSearch(googleSearch);
        webSearchScraping.setBingSearch(bingSearch);
        webSearchScraping.start();

        Set<String> urlSet = webSearchScraping.getUrlList();
        HttpSearchScraping httpSearchScraping = new HttpSearchScraping();
        httpSearchScraping.setUrls(urlSet);
        httpSearchScraping.setFindEmail(true);
        httpSearchScraping.setFindTelephone(true);
        List<Url> urlResults = httpSearchScraping.start();

        urlResults = urlResults.stream().map(e -> {
            e.setKeyword(newKeyword);
            return e;
        }).collect(Collectors.toList());

        newKeyword.getUrls().addAll(urlResults);
        newKeyword.setNumberOfPages(urlResults.size());
        Keyword savedKeyword = keywordService.saveNewKeyword(newKeyword);
        System.out.println("new Keyword saved");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", URL_PATHS.API + savedKeyword.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    public void setGoogleSearch(boolean googleSearch) {
        this.googleSearch = googleSearch;
    }

    public void setBingSearch(boolean bingSearch) {
        this.bingSearch = bingSearch;
    }

    public void setFindNumberOfPages(int findNumberOfPages) {
        this.findNumberOfPages = findNumberOfPages;
    }

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }
}
