package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.domain.Keyword;
import com.webcrawler.webcrawlerapp.domain.Url;
import com.webcrawler.webcrawlerapp.utils.HttpSearch;
import com.webcrawler.webcrawlerapp.utils.WebSearch;
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

        WebSearch webSearch = new WebSearch();
        webSearch.setKeyword(newKeyword.getKeyword());
        webSearch.setFindNumberOfPages(10);
        webSearch.setGoogleSearch(googleSearch);
        webSearch.setBingSearch(bingSearch);
        webSearch.start();

        Set<String> urlSet = webSearch.getUrlList();
        HttpSearch httpSearch = new HttpSearch();
        httpSearch.setUrls(urlSet);
        httpSearch.setFindEmail(true);
        httpSearch.setFindTelephone(true);
        List<Url> urlResults = httpSearch.start();

        urlResults = urlResults.stream().map(e -> {
            e.setKeyword(newKeyword);
            return e;
        }).collect(Collectors.toList());

        newKeyword.getUrls().addAll(urlResults);

        keywordService.saveNewKeyword(newKeyword);
        System.out.println("new Keyword saved");

        return new ResponseEntity(HttpStatus.CREATED);
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
