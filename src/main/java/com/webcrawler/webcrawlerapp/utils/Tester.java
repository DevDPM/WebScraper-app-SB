package com.webcrawler.webcrawlerapp.utils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Tester {

    public static void main(String[] args) throws IOException {
        WebSearch webSearch = new WebSearch();
        webSearch.setKeyword("stukadoor");
        webSearch.setFindNumberOfPages(50);
        webSearch.setGoogleSearch(true);
        webSearch.setBingSearch(true);
        webSearch.start();
        Set<String> urlSet = webSearch.getUrlList();

        HttpSearch httpSearch = new HttpSearch();
        httpSearch.setUrls(urlSet);

//        Set<String> mock = new HashSet<>();
//        mock.add("https://svanrooijenstukadoors.nl");
//        httpSearch.setUrls(mock);

        httpSearch.setFindEmail(true);
        httpSearch.setFindTelephone(true);
        httpSearch.start();
    }
}