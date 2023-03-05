package com.webcrawler.webcrawlerapp.controller;

import com.webcrawler.webcrawlerapp.domain.Email;
import com.webcrawler.webcrawlerapp.domain.Keyword;
import com.webcrawler.webcrawlerapp.domain.Url;
import com.webcrawler.webcrawlerapp.service.EmailService;
import com.webcrawler.webcrawlerapp.service.KeywordService;
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
public class EmailController {

    private final KeywordService keywordService;
    private final EmailService emailService;

    @DeleteMapping(value = URL_PATH.API_KEYWORD_BY_ID_URL_BY_ID_START_DELETION_EMAIL_BY_ID)
    public ResponseEntity deleteEmailByIdByUrlByIdByKeywordById(@PathVariable("keywordId") UUID keywordId,
                                                                @PathVariable("urlId") UUID urlId,
                                                                @PathVariable("emailId") UUID emailId) {
        Keyword keyword = keywordService.getKeywordById(keywordId);
        Optional<Url> url = keyword.getUrls()
                .stream()
                .filter(e -> e.getId().equals(urlId))
                .findFirst();
        Optional<Email> emailToDelete = null;
        if (url.isPresent()) {
            emailToDelete = url.get().getEmailSet()
                    .stream()
                    .filter(e -> e.getId().equals(emailId))
                    .findFirst();
        }
        if (emailToDelete.isPresent()) {
            Email email = emailToDelete.get();
            emailService.deleteByIdAndByUrlByIdAndByKeywordById(email.getId(), url.get().getId(), keyword.getId());
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
