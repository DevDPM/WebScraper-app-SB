package com.webcrawler.webcrawlerapp.repository;

import com.webcrawler.webcrawlerapp.domain.PhoneNumber;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PhoneNumberRepository extends CrudRepository<PhoneNumber, UUID> {
}
