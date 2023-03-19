package com.webcrawler.webcrawlerapp.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"url"})
@AllArgsConstructor
public class PhoneNumber extends BaseEntity {

    private String phoneNumber;
    private boolean isTrustworthy;
    private int numberOfHits;

    @JsonBackReference
    @ManyToOne
    @JoinColumn (name = "url_id")
    private Url url;

    @Builder
    public PhoneNumber(UUID id, String phoneNumber, int numberOfHits, Url url) {
        super(id);
        this.phoneNumber = phoneNumber;
        this.numberOfHits = numberOfHits;
        this.url = url;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isTrustworthy() {
        return isTrustworthy;
    }

    public void setTrustworthy(boolean trustworthy) {
        isTrustworthy = trustworthy;
    }

    public int getNumberOfHits() {
        return numberOfHits;
    }

    public void setNumberOfHits(int numberOfHits) {
        this.numberOfHits = numberOfHits;
    }

    public Url getUrl() {
        return url;
    }

    public void setUrl(Url url) {
        this.url = url;
    }
}
