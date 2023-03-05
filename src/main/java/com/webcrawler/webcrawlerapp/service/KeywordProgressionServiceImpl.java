package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.domain.KeywordProgression;
import com.webcrawler.webcrawlerapp.repository.KeywordProgressionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class KeywordProgressionServiceImpl implements KeywordProgressionService {

    private final KeywordProgressionRepository keywordProgressionRepository;

    public KeywordProgressionServiceImpl(KeywordProgressionRepository keywordProgressionRepository) {
        this.keywordProgressionRepository = keywordProgressionRepository;
    }

    @Override
    public List<KeywordProgression> GetAllKeywordProgression() {
        List<KeywordProgression> keywordProgressions = keywordProgressionRepository.findAll();

        return keywordProgressions;
    }

    public KeywordProgression addKeywordProgress(KeywordProgression keywordProgress) {
        KeywordProgression savedKeywordProgress = keywordProgressionRepository.save(keywordProgress);
        return savedKeywordProgress;
    }

    public void DeleteKeywordProgress(KeywordProgression keywordProgress) {
        keywordProgressionRepository.delete(keywordProgress);
    }

    public KeywordProgression getKeywordProgressById(UUID id) {
        KeywordProgression keywordProgress = keywordProgressionRepository.getById(id);
        return keywordProgress;
    }

    public void UpdateKeywordProgress(KeywordProgression keywordProgress) {
        Optional<KeywordProgression> returnKeywordProgress = GetAllKeywordProgression()
                .stream()
                .filter(e -> e.getId().equals(keywordProgress.getId()))
                .map(e-> {
                    e.setAdditionalInfo(keywordProgress.getAdditionalInfo());
                    e.setEstimatedTime(keywordProgress.getEstimatedTime());
                    e.setPercentageCompleted(keywordProgress.getPercentageCompleted());
                    return e;
                }).findFirst();

        if (!returnKeywordProgress.isPresent())
            return;

        keywordProgressionRepository.save(returnKeywordProgress.get());
    }

    @Override
    public void deleteKeywordProgressionById(UUID keywordProgressionId) {
        keywordProgressionRepository.deleteById(keywordProgressionId);
    }
}
