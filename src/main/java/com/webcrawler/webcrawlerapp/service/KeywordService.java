package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.domain.Keyword;

import java.util.List;
import java.util.UUID;

public interface KeywordService {
    List<Keyword> listKeywords();
    Keyword getKeywordById(UUID id);

    Keyword saveNewKeyword(Keyword newKeyword);
}
