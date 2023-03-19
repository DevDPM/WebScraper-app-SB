package com.webcrawler.webcrawlerapp.controller;

import com.webcrawler.webcrawlerapp.domain.*;
import com.webcrawler.webcrawlerapp.service.ioManager;
import com.webcrawler.webcrawlerapp.service.KeywordProgressionService;
import com.webcrawler.webcrawlerapp.service.KeywordService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:4200") // Angular frontend
@AllArgsConstructor
@RestController
public class KeywordController {
    private final KeywordProgressionService keywordProgressionService;
    private final KeywordService keywordService;
    private final ioManager ioManager;

    @PostMapping(value = URL_PATH.API_START_CRAWLING)
    public ResponseEntity handlePost(@RequestBody Keyword crawlKeyword) {

        Keyword keyword = new Keyword();
        keyword.setKeyword(crawlKeyword.getKeyword());
        keyword.setBingSearch(crawlKeyword.isBingSearch());
        keyword.setGoogleSearch(crawlKeyword.isGoogleSearch());
        keyword.setYahooSearch(crawlKeyword.isYahooSearch());
        keyword.setScrapeEmail(crawlKeyword.isScrapeEmail());
        keyword.setScrapePhoneNumber(crawlKeyword.isScrapePhoneNumber());
        keyword.setNumberOfPages(crawlKeyword.getNumberOfPages());
        Keyword savingKeyword = keywordService.saveNeworUpdateKeyword(keyword);
        System.out.println("getKeyword : " + savingKeyword.getKeyword() + " " + savingKeyword.getId());
        System.out.println("isBingSearch : " + savingKeyword.isBingSearch() + " " + savingKeyword.getId());
        System.out.println("isGoogleSearch : " + savingKeyword.isGoogleSearch() + " " + savingKeyword.getId());
        System.out.println("isScrapeEmail : " + savingKeyword.isScrapeEmail() + " " + savingKeyword.getId());
        System.out.println("isYahooSearch : " + savingKeyword.isYahooSearch() + " " + savingKeyword.getId());
        System.out.println("isScrapePhoneNumber : " + savingKeyword.isScrapePhoneNumber() + " " + savingKeyword.getId());
        System.out.println("getNumberOfPages : " + savingKeyword.getNumberOfPages() + " " + savingKeyword.getId());

        KeywordProgression tracker = new KeywordProgression();

        ioManager.scrapeRootDomainUrlFromSearchEngine(keyword);
        ioManager.scrapeRootDomainHttp(keyword);

        Keyword savedKeyword = keywordService.getKeywordById(keyword.getId());

        System.out.println("saved" + savedKeyword.getKeyword() + savedKeyword.getId());

        savedKeyword.getUrls().forEach(e -> {
            e.getEmailSet().forEach(System.out::println);
            e.getPhoneNumberSet().forEach(System.out::println);
        });

        List<KeywordProgression> progressList = keywordProgressionService.GetAllKeywordProgression();

        return new ResponseEntity<>(progressList, HttpStatus.OK);
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
