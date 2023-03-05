package com.webcrawler.webcrawlerapp.utils;

import com.webcrawler.webcrawlerapp.domain.KeywordProgression;
import com.webcrawler.webcrawlerapp.service.KeywordProgressionService;
import com.webcrawler.webcrawlerapp.utils.searchEngines.SearchEngine;
import com.webcrawler.webcrawlerapp.utils.searchEngines.SearchEngineFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.webcrawler.webcrawlerapp.utils.Element.*;


public class WebSearchScraping {

    public enum Engine {
        GOOGLE,
        BING;

    }

    private final KeywordProgressionService keywordProgressionService;
    private KeywordProgression keywordProgression;
    private Set<String> urlList;
    private String keyword;
    private int numberOfHttpPages;
    private boolean googleSearch;
    private boolean bingSearch;
    private Set<Engine> engines;

    public WebSearchScraping(KeywordProgressionService keywordProgressionService) {
        this.keywordProgressionService = keywordProgressionService;
        this.urlList = new HashSet<>();
        this.googleSearch = false;
        this.bingSearch = false;
        this.engines = new HashSet<>();
    }


    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setGoogleSearch(boolean setGoogle) {
        this.googleSearch = setGoogle;
    }

    public void setBingSearch(boolean setBing) {
        this.bingSearch = setBing;
    }

    public void start() {

        addSearchEngines();

        if (engines.isEmpty()) {
            System.out.println("No searchEngine");
            return;
        }

        for (Engine engine : engines) {

            keywordProgression.setAdditionalInfo("Start searching: " + engine.toString().toLowerCase()+ "..");
            keywordProgressionService.UpdateKeywordProgress(keywordProgression);

            WebDocument document = new WebDocument();

            SearchEngineFactory sef = new SearchEngineFactory();
            SearchEngine searchEngine = sef.getSearchEngine(engine);


            if (getKeyword() == "" ||
                    getKeyword() == null ||
                    getKeyword().isEmpty() ||
                    getNumberOfHttpPages() < 0)
                break;

            String searchUrl = searchEngine.getSearchUrl(replaceSpaceByPlus(getKeyword()), getNumberOfHttpPages());

            System.out.println(searchUrl);
            try {
                document.crawlUrl(searchUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Element.setWebDocument(document);

            List<String> foundUrls = Element.getElementListFromElementHTML(Tag.HYPER_LINK, Tag.HTML).stream()
                    .filter(e -> Element.containAttribute(Attribute.HREF, e))
                    .map(e -> {
                        return Element.getAttributeContent(Attribute.HREF, e);
                    })
                    .collect(Collectors.toList());

            addFoundUrlsToUrlList(searchEngine.subtractHttpDomain(foundUrls));
        }
        return;
    }

    private String replaceSpaceByPlus(String keyword) {
        return keyword.replace(" ", "+");
    }

    private boolean addFoundUrlsToUrlList(Set<String> foundUrls) {
        return urlList.addAll(foundUrls);
    }

    private void addSearchEngines() {
        if (isGoogleSearch())
            engines.add(Engine.GOOGLE);
        if (isBingSearch())
            engines.add(Engine.BING);
    }

    public void setKeywordProgression(KeywordProgression keywordProgression) {
        this.keywordProgression = keywordProgression;
    }

    private int getNumberOfHttpPages() {
        return numberOfHttpPages;
    }

    public void setFindNumberOfPages(int numberOfHttpPages) {
        this.numberOfHttpPages = numberOfHttpPages;
    }

    public Set<String> getUrlList() {
        return urlList;
    }

    private boolean isGoogleSearch() {
        return this.googleSearch;
    }

    private boolean isBingSearch() {
        return bingSearch;
    }

    private String getKeyword() {
        return this.keyword;
    }
}