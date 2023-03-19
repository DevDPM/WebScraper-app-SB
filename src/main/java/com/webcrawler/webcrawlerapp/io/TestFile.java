package com.webcrawler.webcrawlerapp.io;

import com.webcrawler.webcrawlerapp.domain.*;

public class TestFile {

    public static void main(String[] args) {

//        scrapeTest();
//        urlTest();


    }

//    private static void scrapeTest() {
//        Keyword keyword = new Keyword();
//        Settings settings = new Settings();
//        KeywordProgression tracker = new KeywordProgression();
//
//        keyword.setKeyword("stukadoor");
//
//        settings.setGoogleSearch(true);
//        settings.setBingSearch(true);
//        settings.setScrapeEmail(true);
//        settings.setYahooSearch(false);
//        settings.setScrapePhoneNumber(true);
//        settings.setNumberOfPages(100);
//
//        CrawlManager manager = new CrawlManager();
//        manager.scrapeRootDomainUrlFromSearchEngine();
//        manager.getKeyword().getUrls().forEach(e -> System.out.println(e.getUrl()));
//        System.out.println(manager.getKeyword().getUrls().size());
//
//        manager.scrapeRootDomainHttp();
//        manager.getKeyword().getUrls()
//                .forEach(e -> {
//                    System.out.println(e.getUrl());
//                    e.getEmailSet().forEach(u -> System.out.println(u.getEmail() +
//                            "\n" + u.getNumberOfHits() +
//                            "\n" + u.isTrustworthy()));
//                    e.getPhoneNumberSet().forEach(u -> System.out.println(u.getPhoneNumber() +
//                            "\n" + u.getNumberOfHits() +
//                            "\n" + u.isTrustworthy()));
//                });
//        System.out.println("-");
//        System.out.println(manager.getKeyword().getUrls().size());
//    }

    public static void urlTest() {
        String[] list = new String[]{
                "https://vdlindenstukadoors.nl",
                "https://www.stukadoor-sjaak.nl/"
        };

        for (String urll : list) {
            Url url = new Url();
            url.setUrl(urll);
            HttpScrapeService webCrawler = new HttpScrapeService(url);
            webCrawler.getElementList(Tag.HYPER_LINK).stream()
                    .filter(e -> webCrawler.containAttribute(Attribute.HREF, e))
                    .map(e -> webCrawler.getAttributeContent(Attribute.HREF, e))
                    .forEach(System.out::println);

        }
    }
}
