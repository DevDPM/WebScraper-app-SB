package com.webcrawler.webcrawlerapp.controller;

import com.webcrawler.webcrawlerapp.domain.Keyword;
import com.webcrawler.webcrawlerapp.service.CrawlBotCallableService;
import com.webcrawler.webcrawlerapp.service.KeywordService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@CrossOrigin(origins = "http://localhost:4200") // Angular frontend
@AllArgsConstructor
@RestController
public class KeywordController {
    private final KeywordService keywordService;
    private final CrawlBotCallableService crawlBotCallableService;

    @PostMapping(value = URL_PATH.API_START_CRAWLING)
    public ResponseEntity handlePost(@RequestBody Keyword keyword) throws InterruptedException, ExecutionException {

//        crawlBotCallableService.setBingSearch(keyword.isBingSearch());
        crawlBotCallableService.setBingSearch(true);
//        crawlBotCallableService.setGoogleSearch(keyword.isGoogleSearch());
        crawlBotCallableService.setGoogleSearch(true);
//        crawlBotCallableService.setFindNumberOfPages(keyword.getNumberOfPages());
        crawlBotCallableService.setFindNumberOfPages(5);
        crawlBotCallableService.setKeyword(keyword);

        System.out.println("task started");
        System.out.println(keyword.getKeyword());

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

    @GetMapping(value = {URL_PATH.API})
    public ResponseEntity<List<Keyword>> listKeywords(@RequestParam(value = "keyword", required = false) String keyword) {
        List<Keyword> keywordList = keywordService.listKeywords(keyword);
        return new ResponseEntity<>(keywordList, HttpStatus.OK);
    }

    @GetMapping(value = URL_PATH.API_KEYWORD_BY_ID)
    public ResponseEntity<Keyword> getKeywordById(@PathVariable("keywordId") UUID keywordId) {
        Keyword keyword = keywordService.getKeywordById(keywordId);
        return new ResponseEntity<>(keyword, HttpStatus.OK);
    }

    @DeleteMapping(value = URL_PATH.API_START_DELETION_KEYWORD_BY_ID)
    public ResponseEntity deleteKeywordById(@PathVariable("keywordId") UUID keywordId) {
        keywordService.deleteKeywordById(keywordId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
