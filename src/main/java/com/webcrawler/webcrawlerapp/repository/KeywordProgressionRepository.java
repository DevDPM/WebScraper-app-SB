package com.webcrawler.webcrawlerapp.repository;

import com.webcrawler.webcrawlerapp.domain.KeywordProgression;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface KeywordProgressionRepository extends JpaRepository<KeywordProgression, UUID> {
}
