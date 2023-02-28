package com.webcrawler.webcrawlerapp.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
@NoArgsConstructor
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private UUID id;
}
