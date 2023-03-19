package com.webcrawler.webcrawlerapp.io.detailScraper;

import com.webcrawler.webcrawlerapp.domain.*;
import com.webcrawler.webcrawlerapp.io.HttpScrapeService;

import java.util.HashSet;
import java.util.Set;

import static com.webcrawler.webcrawlerapp.io.detailScraper.Detail.EMAIL;
import static com.webcrawler.webcrawlerapp.io.detailScraper.Detail.PHONE_NUMBER;

public class ScrapeDetailFactory {

    public ScrapeDetail<Url, HttpScrapeService> getScraper(Detail detail) {
        if (detail == null)
            return null;

        return switch (detail) {
            case EMAIL -> new ScrapeEmail();
            case PHONE_NUMBER -> new ScrapePhoneNumber();
            default -> throw new IllegalArgumentException("Unknown scrape action");
        };
    }

    public Set<Detail> getDetailObject(Keyword keyword) {

        Set<Detail> scrapeDetails = new HashSet<>();

        if (keyword == null)
            return null;

        if (keyword.isScrapeEmail())
            scrapeDetails.add(EMAIL);

        if (keyword.isScrapePhoneNumber())
            scrapeDetails.add(PHONE_NUMBER);

        return scrapeDetails;
    }
}
