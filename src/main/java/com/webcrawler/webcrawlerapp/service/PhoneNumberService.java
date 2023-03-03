package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.domain.Email;
import com.webcrawler.webcrawlerapp.domain.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PhoneNumberService {
    void deleteByIdAndByUrlByIdAndByKeywordById(UUID phoneNumberId, UUID urlId, UUID keywordId);
}
