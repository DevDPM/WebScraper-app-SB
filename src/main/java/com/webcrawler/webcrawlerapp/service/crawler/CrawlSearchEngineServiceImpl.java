package com.webcrawler.webcrawlerapp.service.crawler;



import com.webcrawler.webcrawlerapp.domain.KeywordProgression;
import com.webcrawler.webcrawlerapp.service.crawler.searchEngines.SearchEngine;
import com.webcrawler.webcrawlerapp.service.crawler.searchEngines.SearchEngineFactory;
import com.webcrawler.webcrawlerapp.utils.Element;
import com.webcrawler.webcrawlerapp.utils.WebDocument;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CrawlSearchEngineServiceImpl implements CrawlSearchEngineService {

    public enum Engine {
        GOOGLE,
        BING;
    }

    private KeywordProgression keywordProgression;
    private Set<String> urlList = new HashSet<>();
    private String keyword;
    private int numberOfHttpPages;
    private boolean googleSearch = false;
    private boolean bingSearch = false;
    private Set<Engine> engines = new HashSet<>();


    @Override
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void setGoogleSearch(boolean setGoogle) {
        this.googleSearch = setGoogle;
    }

    @Override
    public void setBingSearch(boolean setBing) {
        this.bingSearch = setBing;
    }

    @Override
    public void start() {
        addSearchEngines();

        if (engines.isEmpty()) {
            System.out.println("No searchEngine");
            return;
        }

        for (Engine engine : engines) {

            keywordProgression.setAdditionalInfo("Start searching: " + engine.toString().toLowerCase()+ "..");

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

            List<String> foundUrls = Element.getElementListFromElementHTML(Element.Tag.HYPER_LINK, Element.Tag.HTML).stream()
                    .filter(e -> Element.containAttribute(Element.Attribute.HREF, e))
                    .map(e -> {
                        return Element.getAttributeContent(Element.Attribute.HREF, e);
                    })
                    .collect(Collectors.toList());

            addFoundUrlsToUrlList(searchEngine.subtractHttpDomain(foundUrls));
        }
        return;
    }

    @Override
    public String replaceSpaceByPlus(String keyword) {
        return keyword.replace(" ", "+");
    }

    @Override
    public boolean addFoundUrlsToUrlList(Set<String> foundUrls) {
        return urlList.addAll(foundUrls);
    }

    @Override
    public void addSearchEngines() {
        if (isGoogleSearch())
            engines.add(Engine.GOOGLE);
        if (isBingSearch())
            engines.add(Engine.BING);
    }

    @Override
    public void setKeywordProgression(KeywordProgression keywordProgression) {
        this.keywordProgression = keywordProgression;
    }

    @Override
    public int getNumberOfHttpPages() {
        return numberOfHttpPages;
    }

    @Override
    public void setFindNumberOfPages(int numberOfHttpPages) {
        this.numberOfHttpPages = numberOfHttpPages;
    }

    @Override
    public Set<String> getUrlList() {
        return urlList;
    }

    @Override
    public boolean isGoogleSearch() {
        return this.googleSearch;
    }

    @Override
    public boolean isBingSearch() {
        return bingSearch;
    }

    @Override
    public String getKeyword() {
        return this.keyword;
    }
}
