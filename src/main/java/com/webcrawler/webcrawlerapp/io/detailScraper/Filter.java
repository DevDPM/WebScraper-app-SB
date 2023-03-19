package com.webcrawler.webcrawlerapp.io.detailScraper;

public class Filter {

    private static final String[] FILTER_LIST = {
            "google", "bing", "youtube", "facebook",
            "instagram", "twitter", "wikipedia", "roc",
            "indeed", "adidas", "perrysport", "intersport",
            "zalando", "decathlon", "amazon", "microsoft",
            "nu", "vacatures", "marktplaats", "pinterest",
            "linkedin", "trustpilot"};

    public static String[] getFilterList() {
        return FILTER_LIST;
    }
}
