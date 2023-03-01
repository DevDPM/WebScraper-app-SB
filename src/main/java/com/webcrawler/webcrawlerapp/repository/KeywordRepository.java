package com.webcrawler.webcrawlerapp.repository;

import com.webcrawler.webcrawlerapp.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface KeywordRepository extends JpaRepository<Keyword, UUID> {
}
