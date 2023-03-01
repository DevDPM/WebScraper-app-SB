package com.webcrawler.webcrawlerapp.utils;

import java.io.IOException;
import java.util.Set;

public class Tester {

    public static void main(String[] args) throws IOException {
        WebSearchScraping webSearchScraping = new WebSearchScraping();
        webSearchScraping.setKeyword("stukadoor");
        webSearchScraping.setFindNumberOfPages(50);
        webSearchScraping.setGoogleSearch(true);
        webSearchScraping.setBingSearch(true);
        webSearchScraping.start();
        Set<String> urlSet = webSearchScraping.getUrlList();

        HttpSearchScraping httpSearchScraping = new HttpSearchScraping();
        httpSearchScraping.setUrls(urlSet);

//        Set<String> mock = new HashSet<>();
//        mock.add("https://svanrooijenstukadoors.nl");
//        httpSearch.setUrls(mock);

        httpSearchScraping.setFindEmail(true);
        httpSearchScraping.setFindTelephone(true);
        httpSearchScraping.start();
    }
}