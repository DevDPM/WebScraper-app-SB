package com.webcrawler.webcrawlerapp.controller;

import com.webcrawler.webcrawlerapp.domain.inspectedUrl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class searchEngineCrawlerServiceImpl implements searchEngineCrawlerService {

    @Override
    public Set<inspectedUrl> getUrlsBySearchEngine(String searchEngine) {
        Set<inspectedUrl> foundUrlSet = new HashSet<>();

        // todo retrieve urls

        return foundUrlSet;
    }
}
