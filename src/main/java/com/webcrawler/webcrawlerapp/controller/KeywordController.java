package com.webcrawler.webcrawlerapp.controller;

import com.webcrawler.webcrawlerapp.domain.Keyword;
import com.webcrawler.webcrawlerapp.service.CrawlBotCallableService;
import com.webcrawler.webcrawlerapp.service.KeywordService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@AllArgsConstructor
@RestController
public class KeywordController {
    private final KeywordService keywordService;
    private final CrawlBotCallableService crawlBotCallableService;

    @PostMapping(value = URL_PATHS.API_START_CRAWLING)
    public ResponseEntity handlePost(@RequestBody Keyword keyword) throws InterruptedException, ExecutionException {

        crawlBotCallableService.setBingSearch(keyword.isBingSearch());
        crawlBotCallableService.setGoogleSearch(keyword.isGoogleSearch());
        crawlBotCallableService.setFindNumberOfPages(keyword.getNumberOfPages());
        crawlBotCallableService.setKeyword(keyword);

        System.out.println("task started");
        System.out.println("");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<ResponseEntity> returnedValues = executorService.submit(() -> {
            return crawlBotCallableService.call();
        });

        int minutes = 0;
        while(!returnedValues.isDone()) {
            System.out.println("time waiting: " + minutes + " minutes.");
            minutes++;
            Thread.sleep(60000);
        }

        ResponseEntity response = returnedValues.get();

        return response;
    }

    @GetMapping(value = {URL_PATHS.API_URL})
    public List<Keyword> listKeywords(@RequestParam(value = "keyword", required = false) String keyword) {
        return keywordService.listKeywords(keyword);
    }

    @GetMapping(value = URL_PATHS.API_KEYWORD_BY_ID)
    public Keyword getKeywordById(@PathVariable("keywordId") UUID keywordId) {
        return keywordService.getKeywordById(keywordId);
    }


}
