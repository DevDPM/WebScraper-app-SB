package com.webcrawler.webcrawlerapp.domain;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class KeywordProgression extends BaseEntity {

    private UUID id;
    private String keyword;
    private String percentageCompleted;
    private String additionalInfo;
    private String estimatedTime;
}
