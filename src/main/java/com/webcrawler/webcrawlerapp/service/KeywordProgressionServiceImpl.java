package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.domain.KeywordProgression;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KeywordProgressionServiceImpl implements KeywordProgressionService {

    private List<KeywordProgression> keywordProgressionList = new ArrayList<>();

    @Override
    public List<KeywordProgression> GetAllKeywordProgression() {

        return keywordProgressionList;
    }

    @Override
    public KeywordProgression addKeywordProgress(KeywordProgression keywordProgress) {

        UUID newId;
        while (true) {
            newId = generateRandomUUID();
            UUID finalNewId = newId;
            Optional<KeywordProgression> idMatch = keywordProgressionList
                    .stream()
                    .filter(keywordProgression -> {
                        return keywordProgression.getId().equals(finalNewId);
                    }).findFirst();
            if (idMatch.isEmpty())
                break;
        }

        KeywordProgression newKeywordProgress = keywordProgress;
        newKeywordProgress.setId(newId);

        keywordProgressionList.add(newKeywordProgress);

        return newKeywordProgress;
    }

    private UUID generateRandomUUID() {
        return UUID.randomUUID();
    }

    @Override
    public void DeleteKeywordProgress(KeywordProgression keywordProgression) {
        keywordProgressionList.remove(keywordProgression);

    }

    public KeywordProgression getKeywordProgressById(UUID id) {
        Optional<KeywordProgression> keywordProgression = keywordProgressionList.stream()
                .filter(kp -> {
                    return kp.getId().equals(id);
                }).findFirst();

        return keywordProgression.orElse(null);
    }

    public KeywordProgression UpdateKeywordProgress(KeywordProgression keywordProgress) {

        Optional<KeywordProgression> returnKeywordProgress = keywordProgressionList
                .stream()
                .filter(kpFind -> {
                    return kpFind.getId().equals(keywordProgress.getId());
                })
                .map(kpConvert -> {
                    kpConvert.setKeyword(keywordProgress.getKeyword());
                    kpConvert.setEstimatedTime(keywordProgress.getEstimatedTime());
                    kpConvert.setAdditionalInfo(keywordProgress.getAdditionalInfo());
                    kpConvert.setPercentageCompleted(keywordProgress.getPercentageCompleted());
                    return kpConvert;
                })
                .findFirst();

        if (returnKeywordProgress.isEmpty())
            return null;

        return returnKeywordProgress.get();
    }

    @Override
    public void deleteKeywordProgressionById(UUID id) {
        Optional<KeywordProgression> kpToDelete = keywordProgressionList
                .stream()
                .filter(kp -> {
                    return kp.getId().equals(id);
                }).findFirst();

        if (kpToDelete.isEmpty())
            return;

        keywordProgressionList.remove(kpToDelete.get());
    }
}
