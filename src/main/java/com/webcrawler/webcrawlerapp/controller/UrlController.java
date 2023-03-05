package com.webcrawler.webcrawlerapp.controller;

import com.webcrawler.webcrawlerapp.domain.Keyword;
import com.webcrawler.webcrawlerapp.domain.Url;
import com.webcrawler.webcrawlerapp.service.KeywordService;
import com.webcrawler.webcrawlerapp.service.UrlService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200") // Angular frontend
@AllArgsConstructor
@RestController
public class UrlController {

    private final KeywordService keywordService;
    private final UrlService urlService;

    @DeleteMapping(value = URL_PATH.API_KEYWORD_BY_ID_START_DELETION_URL_BY_ID)
    public ResponseEntity deleteUrlByIdFromKeywordById(@PathVariable("keywordId") UUID keywordId,
                                                               @PathVariable("urlId") UUID urlId) {
        Keyword keyword = keywordService.getKeywordById(keywordId);
        Optional<Url> urlToDelete = keyword.getUrls()
                .stream()
                .filter(e -> e.getId().equals(urlId))
                .findFirst();

        if (urlToDelete.isPresent()) {
            Url url = urlToDelete.get();
            urlService.deleteByIdAndKeywordId(url.getId(), keyword.getId());
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
