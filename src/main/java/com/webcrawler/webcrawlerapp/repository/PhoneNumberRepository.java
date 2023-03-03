package com.webcrawler.webcrawlerapp.repository;

import com.webcrawler.webcrawlerapp.domain.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, UUID> {
}
