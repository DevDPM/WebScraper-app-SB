package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.domain.Keyword;
import com.webcrawler.webcrawlerapp.repository.KeywordRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class KeywordServiceImpl implements KeywordService {

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
    public Keyword saveNeworUpdateKeyword(Keyword newKeyword) {
        return keywordRepository.save(newKeyword);
    }

    @Override
    public void deleteKeywordById(UUID keywordId) {
        keywordRepository.deleteById(keywordId);
    }

    public KeywordServiceImpl(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }
}
