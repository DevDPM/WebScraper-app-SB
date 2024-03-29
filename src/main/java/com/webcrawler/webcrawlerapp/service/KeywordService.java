package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.domain.Keyword;

import java.util.List;
import java.util.UUID;

public interface KeywordService {
    List<Keyword> listKeywords(String keyword);
    Keyword getKeywordById(UUID id);

    Keyword saveNeworUpdateKeyword(Keyword keyword);

    void deleteKeywordById(UUID keywordId);

}
