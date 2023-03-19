package com.webcrawler.webcrawlerapp.io.searchEngines;

import com.webcrawler.webcrawlerapp.domain.Keyword;

import java.util.HashSet;
import java.util.Set;

import static com.webcrawler.webcrawlerapp.io.searchEngines.Organization.*;

public class SearchEngineFactory {

    public SearchEngine getSearchEngine(Organization organization) {
        if (organization == null)
            return null;

        return switch (organization) {
            case GOOGLE -> new GoogleSearchEngine();
            case BING -> new BingSearchEngine();
            case YAHOO -> new YahooSearchEngine();
            default -> throw new IllegalArgumentException("Unknown engine");
        };
    }

    public Set<Organization> getSearchEngineObject(Keyword keyword) {

        Set<Organization> organizations = new HashSet<>();

        if (keyword == null)
            return null;

        if (keyword.isGoogleSearch())
            organizations.add(GOOGLE);

        if (keyword.isBingSearch())
            organizations.add(BING);

        if (keyword.isYahooSearch())
            organizations.add(YAHOO);

        return organizations;
    }
}
