package com.webcrawler.webcrawlerapp.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"url"})
@AllArgsConstructor
public class PhoneNumber extends BaseEntity {

    private String phoneNumber;
    private Integer numberOfHits;

    @JsonBackReference
    @ManyToOne
    @JoinColumn (name = "url_id")
    private Url url;

    @Builder
    public PhoneNumber(UUID id, String phoneNumber, Integer numberOfHits, Url url) {
        super(id);
        this.phoneNumber = phoneNumber;
        this.numberOfHits = numberOfHits;
        this.url = url;
    }
}
