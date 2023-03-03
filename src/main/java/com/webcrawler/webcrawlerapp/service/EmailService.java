package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailService {
    void deleteByIdAndByUrlByIdAndByKeywordById(UUID emailId, UUID urlId, UUID keywordId);
}
