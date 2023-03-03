package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.domain.Email;
import com.webcrawler.webcrawlerapp.domain.Url;
import com.webcrawler.webcrawlerapp.repository.EmailRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailService {

    private final UrlService urlService;
    private final EmailRepository emailRepository;

    public EmailServiceImpl(UrlService urlService, EmailRepository emailRepository) {
        this.urlService = urlService;
        this.emailRepository = emailRepository;
    }

    @Override
    public void deleteByIdAndByUrlByIdAndByKeywordById(UUID emailId, UUID urlId, UUID keywordId) {
        Url url = urlService.getUrlByIdByKeywordById(urlId, keywordId);
        if (url == null)
            new RuntimeException("no valid url or keyword");

        Email email = getEmailById(emailId);
        if (email == null)
            new RuntimeException("no valid email");

        url.getEmailSet().remove(email);

        emailRepository.deleteById(emailId);
    }

    public Email getEmailById(UUID emailId) {
        return emailRepository.findById(emailId).orElse(null);
    }
}
