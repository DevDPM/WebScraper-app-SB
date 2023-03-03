package com.webcrawler.webcrawlerapp.controller;

public final class URL_PATHS {

    public static final String API = "/api/v1/keyword";

    private static final String START_CRAWLING = "/crawl";
    private static final String START_DELETION = "/delete";
    private static final String KEYWORD_BY_ID = "/{keywordId}";
    private static final String URL_BY_ID = "/{urlId}";
    private static final String PHONENUMBER_BY_ID = "/phoneNumber/{phoneNumberId}";
    private static final String EMAIL_BY_ID = "/email/{emailId}";

    public static final String API_START_CRAWLING = API + START_CRAWLING;
    public static final String API_KEYWORD_BY_ID = API + KEYWORD_BY_ID;
    public static final String API_KEYWORD_BY_ID_START_DELETION_URL_BY_ID = API + KEYWORD_BY_ID + START_DELETION + URL_BY_ID;
    public static final String API_START_DELETION_KEYWORD_BY_ID = API + START_DELETION + KEYWORD_BY_ID;
    public static final String API_KEYWORD_BY_ID_URL_BY_ID_START_DELETION_PHONENUMBER_BY_ID =
            API + KEYWORD_BY_ID + URL_BY_ID + START_DELETION + PHONENUMBER_BY_ID;

    public static final String API_KEYWORD_BY_ID_URL_BY_ID_START_DELETION_EMAIL_BY_ID =
            API + KEYWORD_BY_ID + URL_BY_ID + START_DELETION + EMAIL_BY_ID;

}
