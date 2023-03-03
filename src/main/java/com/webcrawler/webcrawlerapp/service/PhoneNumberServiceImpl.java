package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.domain.Email;
import com.webcrawler.webcrawlerapp.domain.PhoneNumber;
import com.webcrawler.webcrawlerapp.domain.Url;
import com.webcrawler.webcrawlerapp.repository.PhoneNumberRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {

    private final UrlService urlService;
    private final PhoneNumberRepository phoneNumberRepository;

    public PhoneNumberServiceImpl(UrlService urlService, PhoneNumberRepository phoneNumberRepository) {
        this.urlService = urlService;
        this.phoneNumberRepository = phoneNumberRepository;
    }

    @Override
    public void deleteByIdAndByUrlByIdAndByKeywordById(UUID phoneNumberId, UUID urlId, UUID keywordId) {
        Url url = urlService.getUrlByIdByKeywordById(urlId, keywordId);
        if (url == null)
            new RuntimeException("no valid url or keyword");

        PhoneNumber phoneNumber = getPhoneNumberById(phoneNumberId);
        if (phoneNumber == null)
            new RuntimeException("no valid email");

        url.getPhoneNumberSet().remove(phoneNumber);

        phoneNumberRepository.deleteById(phoneNumberId);
    }

    public PhoneNumber getPhoneNumberById(UUID emailId) {
        return phoneNumberRepository.findById(emailId).orElse(null);
    }
}
