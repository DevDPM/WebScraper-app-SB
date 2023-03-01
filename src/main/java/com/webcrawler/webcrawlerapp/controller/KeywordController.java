package com.webcrawler.webcrawlerapp.controller;

import com.webcrawler.webcrawlerapp.domain.Keyword;
import com.webcrawler.webcrawlerapp.domain.Url;
import com.webcrawler.webcrawlerapp.service.CrawlBotCallableService;
import com.webcrawler.webcrawlerapp.service.KeywordService;
import com.webcrawler.webcrawlerapp.utils.HttpSearch;
import com.webcrawler.webcrawlerapp.utils.WebSearch;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/keyword")
public class KeywordController {
    private final KeywordService keywordService;
    private final CrawlBotCallableService crawlBotCallableService;

    @PostMapping(value = "/crawling")
    public ResponseEntity handlePost(@RequestBody Keyword keyword) throws IOException, InterruptedException {

        crawlBotCallableService.setBingSearch(false);
        crawlBotCallableService.setGoogleSearch(true);
        crawlBotCallableService.setFindNumberOfPages(10);
        System.out.println("Controller keyword: "+keyword.getKeyword());
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

        return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @GetMapping(value = {"","/"})
    public List<Keyword> listKeywords() {
        return keywordService.listKeywords();
    }

    @GetMapping(value = "{keywordId}")
    public Keyword getKeywordById(@PathVariable("keywordId") UUID keywordId) {
        return keywordService.getKeywordById(keywordId);
    }

}
