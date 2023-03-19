package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.domain.*;
import com.webcrawler.webcrawlerapp.io.HttpScrapeService;
import com.webcrawler.webcrawlerapp.io.SearchEngineService;
import com.webcrawler.webcrawlerapp.io.detailScraper.Detail;
import com.webcrawler.webcrawlerapp.io.detailScraper.ScrapeDetail;
import com.webcrawler.webcrawlerapp.io.detailScraper.ScrapeDetailFactory;
import com.webcrawler.webcrawlerapp.io.searchEngines.Organization;
import com.webcrawler.webcrawlerapp.io.searchEngines.SearchEngine;
import com.webcrawler.webcrawlerapp.io.searchEngines.SearchEngineFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

@Service
public class ioManager {

    private final KeywordService keywordService;

    public ioManager(KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    public void scrapeRootDomainUrlFromSearchEngine(Keyword keyword) {

        Keyword crawlKeyword = keywordService.getKeywordById(keyword.getId());

        if (crawlKeyword == null)
            return;

        SearchEngineFactory sef = new SearchEngineFactory();
        Set<Organization> organizationSet = sef.getSearchEngineObject(crawlKeyword);
        if (organizationSet.isEmpty())
            throw new RuntimeException("No organization selected");

        int cpuCount = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(cpuCount);
        ArrayList<Future<Set<Url>>> results = new ArrayList<>();

        for (Organization organization : organizationSet) {
            results.add(service.submit(new Callable<Set<Url>>() {
                @Override
                public Set<Url> call() throws Exception {
                    SearchEngine searchEngine = sef.getSearchEngine(organization);
                    String searchEngineUrl = searchEngine.getSearchUrl(crawlKeyword);
                    System.out.println(searchEngineUrl);
                    return new SearchEngineService(searchEngineUrl).getRootDomainUrlSet(crawlKeyword);
                }
            }));
        }

        service.shutdown();

        Set<Url> resultSet = new HashSet<>();
        for (Future<Set<Url>> result : results) {
            try {
                Set<Url> urlResults = result.get();
                System.out.println(urlResults.size());
                resultSet.addAll(urlResults);
            } catch (InterruptedException | ExecutionException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(resultSet.size());
        crawlKeyword.getUrls().addAll(resultSet);
        System.out.println(crawlKeyword.getUrls().size());
        ///////////////////////////////////
        service = null;
        results = null;
        resultSet = null;
        ///////////////////////////////////

        System.gc();

        keywordService.saveNeworUpdateKeyword(crawlKeyword);
    }

    public void scrapeRootDomainHttp(Keyword keyword) {

        Keyword crawlKeyword = keywordService.getKeywordById(keyword.getId());

        if (crawlKeyword == null)
            return;


        int cpuCount = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(cpuCount);
        ArrayList<Future<Url>> results = new ArrayList<>();

        for (Url rootUrl : crawlKeyword.getUrls()) {
            results.add(service.submit(new Callable<Url>() {
                @Override
                public Url call() throws Exception {
                    HttpScrapeService httpScrapeService = new HttpScrapeService(rootUrl);
                    List<String> childUrlList = httpScrapeService.scrapeChildUrlList();

                    int i = 0;

                    for (String childUrl : childUrlList) {
                        Url url = new Url(childUrl);

                        System.out.println(rootUrl.getUrl() + "  --  " + url.getUrl() + ": " + ++i);

                        ScrapeDetailFactory sf = new ScrapeDetailFactory();
                        Set<Detail> scrapeDetails = new HashSet<>(sf.getDetailObject(crawlKeyword));

                        Set<Detail> doneScraping = new HashSet<>();
                        for (Detail detail : scrapeDetails) {
                            ScrapeDetail<Url, HttpScrapeService> scraper = sf.getScraper(detail);
                            scraper.scrapeDetailsToUrl(rootUrl, new HttpScrapeService(url));

                            if (scraper.foundReliableDetail()) {
                                doneScraping.add(detail);
                                if (doneScraping.size() == scrapeDetails.size())
                                    break;
                            }
                        }

                        rootUrl.getEmailSet().forEach(e -> System.out.println(e.getEmail()));
                        rootUrl.getPhoneNumberSet().forEach(e -> System.out.println(e.getPhoneNumber()));

                        if (i >= 20)
                            break;
                    }
                    System.gc();
                    return rootUrl;
                }
            }));
        }

        service.shutdown();

        Set<Url> resultSet = new HashSet<>();
        for (Future<Url> result : results) {
            try {
                resultSet.add(result.get());
            } catch (InterruptedException | ExecutionException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("inserting results to entities now");

        keywordService.saveNeworUpdateKeyword(crawlKeyword);
    }
}
