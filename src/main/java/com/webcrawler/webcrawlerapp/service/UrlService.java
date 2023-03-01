package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.domain.Keyword;
import com.webcrawler.webcrawlerapp.domain.Url;

import java.util.List;
import java.util.UUID;

public interface UrlService {

    List<Url> listUrlByKeyword(Keyword keyword);

    Url getUrlById(UUID id);

}