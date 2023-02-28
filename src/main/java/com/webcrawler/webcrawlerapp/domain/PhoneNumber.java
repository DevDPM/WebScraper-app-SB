package com.webcrawler.webcrawlerapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class PhoneNumber extends BaseEntity {

    private String phoneNumber;
    private Integer numberOfHits;

    @ManyToOne
    @JoinColumn (name = "owner_id")
    private Url url;
}
