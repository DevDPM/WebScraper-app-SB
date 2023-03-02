package com.webcrawler.webcrawlerapp.controller;

public final class URL_PATHS {

    public static final String API = "/api/v1/keyword";

    private static final String START_CRAWLING = "/crawl";
    private static final String START_DELETION = "/delete";
    private static final String KEYWORD_BY_ID = "/{keywordId}";
    private static final String URL_BY_ID = "/{urlId}";

    public static final String API_START_CRAWLING = API + START_CRAWLING;
    public static final String API_KEYWORD_BY_ID = API + KEYWORD_BY_ID;
    public static final String API_KEYWORD_BY_ID_URL_BY_ID = API + KEYWORD_BY_ID + URL_BY_ID;
    public static final String API_START_DELETION_KEYWORD_BY_ID = API + START_DELETION + KEYWORD_BY_ID;

}
