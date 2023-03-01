package com.webcrawler.webcrawlerapp.controller;

public final class URLS {

    public static final String API_URL = "/api/v1/keyword";
    private static final String START_CRAWLING = "/crawling";
    private static final String KEYWORD_BY_ID = "/{keywordId}";
    private static final String URL_BY_ID = "/{urlId}";
    public static final String API_START_CRAWLING = API_URL + START_CRAWLING;
    public static final String API_KEYWORD_BY_ID = API_URL + KEYWORD_BY_ID;
    public static final String API_KEYWORD_BY_ID_URL_BY_ID = API_URL + KEYWORD_BY_ID + URL_BY_ID;

}
