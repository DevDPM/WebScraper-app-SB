package com.webcrawler.webcrawlerapp.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"url"})
@Entity
public class Email extends BaseEntity{

    private String email;
    private boolean isTrustworthy;
    private int numberOfHits;

    @JsonBackReference
    @ManyToOne
    @JoinColumn (name = "url_id")
    private Url url;

    @Builder
    public Email(UUID id, String email, int numberOfHits, Url url) {
        super(id);
        this.email = email;
        this.url = url;
        this.numberOfHits = numberOfHits;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
