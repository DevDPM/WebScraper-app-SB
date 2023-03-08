package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.controller.URL_PATH;
import com.webcrawler.webcrawlerapp.domain.Keyword;
import com.webcrawler.webcrawlerapp.domain.KeywordProgression;
import com.webcrawler.webcrawlerapp.domain.Settings;
import com.webcrawler.webcrawlerapp.domain.Url;
import com.webcrawler.webcrawlerapp.service.crawler.CrawlHttpService;
import com.webcrawler.webcrawlerapp.service.crawler.CrawlSearchEngineService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CrawlServiceImpl implements CrawlService {

    private final KeywordProgressionService keywordProgressionService;
    private final CrawlSearchEngineService crawlSearchEngineService;
    private final CrawlHttpService crawlHttpService;
    private final SettingsService settingsService;
    private final KeywordService keywordService;
    private Keyword keyword;
    private boolean googleSearch;
    private boolean bingSearch;
    private int findNumberOfPages;



    public CrawlServiceImpl(KeywordProgressionService keywordProgressionService,
                            CrawlSearchEngineService crawlSearchEngineService,
                            CrawlHttpService crawlHttpService,
                            SettingsService settingsService,
                            KeywordService keywordService) {
        this.keywordProgressionService = keywordProgressionService;
        this.crawlSearchEngineService = crawlSearchEngineService;
        this.crawlHttpService = crawlHttpService;
        this.settingsService = settingsService;
        this.keywordService = keywordService;
    }
    @Override
    public ResponseEntity call() {

        // set keyword
        Keyword crawlKeyword = getKeyword();

        // set search engine settings
        Settings settings = settingsService.getSetting();
        setBingSearch(settings.isBingSearch());
        setGoogleSearch(settings.isGoogleSearch());
        setFindNumberOfPages(settings.getNumberOfPages());

        // set keyword progression tracker
        KeywordProgression tracker = new KeywordProgression();
        tracker.setKeyword(crawlKeyword.getKeyword());
        tracker.setAdditionalInfo("Crawl search engines.. ");
        tracker.setPercentageCompleted("0");
        tracker.setPercentageCompleted("0%");
        tracker.setEstimatedTime("-");
        KeywordProgression savedTracker = keywordProgressionService.addKeywordProgress(tracker);

        // set crawl search engines and start
        crawlSearchEngineService.setKeyword(crawlKeyword.getKeyword());
        crawlSearchEngineService.setGoogleSearch(googleSearch);
        crawlSearchEngineService.setBingSearch(bingSearch);
        crawlSearchEngineService.setFindNumberOfPages(findNumberOfPages);
        crawlSearchEngineService.setKeywordProgression(savedTracker);
        crawlSearchEngineService.start();


        Set<String> urlSet = crawlSearchEngineService.getUrlList();
        crawlHttpService.setKeywordProgression(savedTracker);
        crawlHttpService.setUrls(urlSet);
        crawlHttpService.setFindEmail(true);
        crawlHttpService.setFindTelephone(true);
        crawlHttpService.start();
        List<Url> results = crawlHttpService.getUrlResultList();

        results = results.stream().map(e -> {
            e.setKeyword(crawlKeyword);
            return e;
        }).collect(Collectors.toList());

        keywordProgressionService.DeleteKeywordProgress(savedTracker);

        crawlKeyword.getUrls().addAll(results);
        crawlKeyword.setNumberOfPages(results.size());
        Keyword savedKeyword = keywordService.saveNewKeyword(crawlKeyword);

        System.out.println("Done crawling " + crawlKeyword.getKeyword());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", URL_PATH.API + savedKeyword.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }


    @Override
    public void setFindNumberOfPages(int findNumberOfPages) {
        this.findNumberOfPages = findNumberOfPages;
    }

    @Override
    public void setBingSearch(boolean bingSearch) {
        this.bingSearch = bingSearch;
    }

    @Override
    public void setGoogleSearch(boolean googleSearch) {
        this.googleSearch = googleSearch;
    }

    @Override
    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }

    public Keyword getKeyword() {
        return keyword;
    }
}
