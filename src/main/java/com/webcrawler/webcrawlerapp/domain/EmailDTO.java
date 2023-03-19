package com.webcrawler.webcrawlerapp.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"url"})
@AllArgsConstructor
public class EmailDTO extends BaseEntity{

    private String email;
    private boolean isTrustworthy;
    private int numberOfHits;

    private UrlDTO url;
}
