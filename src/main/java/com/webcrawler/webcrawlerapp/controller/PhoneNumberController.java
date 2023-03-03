package com.webcrawler.webcrawlerapp.controller;

import com.webcrawler.webcrawlerapp.domain.Email;
import com.webcrawler.webcrawlerapp.domain.Keyword;
import com.webcrawler.webcrawlerapp.domain.PhoneNumber;
import com.webcrawler.webcrawlerapp.domain.Url;
import com.webcrawler.webcrawlerapp.service.EmailService;
import com.webcrawler.webcrawlerapp.service.KeywordService;
import com.webcrawler.webcrawlerapp.service.PhoneNumberService;
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
public class PhoneNumberController {

    private final KeywordService keywordService;
    private final PhoneNumberService phoneNumberService;

    @DeleteMapping(value = URL_PATHS.API_KEYWORD_BY_ID_URL_BY_ID_START_DELETION_PHONENUMBER_BY_ID)
    public ResponseEntity deleteEmailByIdByUrlByIdByKeywordById(@PathVariable("keywordId") UUID keywordId,
                                                                @PathVariable("urlId") UUID urlId,
                                                                @PathVariable("phoneNumerId") UUID phoneNumberId) {
        Keyword keyword = keywordService.getKeywordById(keywordId);
        Optional<Url> url = keyword.getUrls()
                .stream()
                .filter(e -> e.getId().equals(urlId))
                .findFirst();
        Optional<PhoneNumber> phoneNumberToDelete = null;
        if (url.isPresent()) {
            phoneNumberToDelete = url.get().getPhoneNumberSet()
                    .stream()
                    .filter(e -> e.getId().equals(phoneNumberId))
                    .findFirst();
        }
        if (phoneNumberToDelete.isPresent()) {
            PhoneNumber phoneNumber = phoneNumberToDelete.get();
            phoneNumberService.deleteByIdAndByUrlByIdAndByKeywordById(phoneNumber.getId(), url.get().getId(), keyword.getId());
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
