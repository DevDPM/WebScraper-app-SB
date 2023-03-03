package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.domain.Keyword;
import com.webcrawler.webcrawlerapp.domain.Url;
import com.webcrawler.webcrawlerapp.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final KeywordService keywordService;

    public UrlServiceImpl(UrlRepository urlRepository,
                          KeywordService keywordService) {
        this.urlRepository = urlRepository;
        this.keywordService = keywordService;
    }

    @Override
    public Url getUrlById(UUID id) {
        return urlRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteByIdAndKeywordId(UUID urlId, UUID keywordId) {
        Keyword keyword = keywordService.getKeywordById(keywordId);
        keyword.removeUrl(getUrlById(urlId));
        urlRepository.deleteById(urlId);
    }

    @Override
    public Url getUrlByIdByKeywordById(UUID urlId, UUID keywordId) {
        Keyword keyword = keywordService.getKeywordById(keywordId);
        Optional<Url> url = keyword.getUrls().stream().filter(e -> e.getId().equals(urlId)).findFirst();

        return url.orElse(null);
    }
}
