package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.domain.Keyword;
import com.webcrawler.webcrawlerapp.repository.KeywordRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KeywordServiceImpl implements KeywordService {

//    private Map<UUID, Keyword> keywordMap;
    private final KeywordRepository keywordRepository;

    @Override
    public List<Keyword> listKeywords() {
        return keywordRepository.findAll();
//        return new ArrayList<>(keywordMap.values());
    }

    @Override
    public Keyword getKeywordById(UUID id) {
        return keywordRepository.findById(id).orElse(null);
//        return keywordMap.get(id);
    }

    @Override
    public Keyword saveNewKeyword(Keyword newKeyword) {
        return keywordRepository.save(newKeyword);
    }

    public KeywordServiceImpl(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }
}
