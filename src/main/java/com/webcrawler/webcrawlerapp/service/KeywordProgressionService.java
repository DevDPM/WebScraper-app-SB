package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.domain.KeywordProgression;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface KeywordProgressionService {
   List<KeywordProgression> GetAllKeywordProgression();
   KeywordProgression addKeywordProgress(KeywordProgression keywordProgress);
   void DeleteKeywordProgress(KeywordProgression keywordProgress);
   KeywordProgression getKeywordProgressById(UUID id);
   KeywordProgression UpdateKeywordProgress(KeywordProgression keywordProgress);

    void deleteKeywordProgressionById(UUID keywordProgressionId);
}
