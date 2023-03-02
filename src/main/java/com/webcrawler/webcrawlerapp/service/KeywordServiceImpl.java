package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.domain.Keyword;
import com.webcrawler.webcrawlerapp.repository.KeywordRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class KeywordServiceImpl implements KeywordService {

//    private Map<UUID, Keyword> keywordMap;
    private final KeywordRepository keywordRepository;

    @Override
    public List<Keyword> listKeywords(String keyword) {

        List<Keyword> keywordList;

        if (StringUtils.hasText(keyword)) {
            keywordList = keywordRepository.findAllByKeywordIsLikeIgnoreCase("%" + keyword + "%");
        } else {
            keywordList = keywordRepository.findAll();
        }
        return keywordList;
    }

    @Override
    public Keyword getKeywordById(UUID id) {
        return keywordRepository.findById(id).orElse(null);
    }

    @Override
    public Keyword saveNewKeyword(Keyword newKeyword) {
        return keywordRepository.save(newKeyword);
    }

    public KeywordServiceImpl(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }
}
