package com.webcrawler.webcrawlerapp.repository;

import com.webcrawler.webcrawlerapp.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailRepository extends JpaRepository<Email, UUID> {
}
