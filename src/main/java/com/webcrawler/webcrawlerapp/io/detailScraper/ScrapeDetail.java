package com.webcrawler.webcrawlerapp.io.detailScraper;

public interface ScrapeDetail<Url, HttpScrapeService> {

    void scrapeDetailsToUrl(Url url, HttpScrapeService httpScrapeService);
    boolean foundReliableDetail();


}
