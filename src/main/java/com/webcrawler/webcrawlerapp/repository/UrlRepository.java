package com.webcrawler.webcrawlerapp.repository;

import com.webcrawler.webcrawlerapp.domain.Url;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;
import java.util.UUID;


public interface UrlRepository extends CrudRepository<Url, UUID> {

}
