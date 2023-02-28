package com.webcrawler.webcrawlerapp.repository;

import com.webcrawler.webcrawlerapp.domain.Email;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EmailRepository extends CrudRepository<Email, UUID> {
}
