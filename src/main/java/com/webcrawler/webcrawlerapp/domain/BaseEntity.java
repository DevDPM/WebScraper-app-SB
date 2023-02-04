package com.webcrawler.webcrawlerapp.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
abstract class BaseEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private UUID id;
}
