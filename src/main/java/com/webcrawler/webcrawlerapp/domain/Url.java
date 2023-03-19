package com.webcrawler.webcrawlerapp.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"keyword"})
@AllArgsConstructor
public class Url extends BaseEntity{

    private String url;
    private String title;
    private int numberOfChildUrl;
    private Quality quality;
    private boolean foundEmail;
    private boolean foundPhoneNumber;
    private String urlError = "None";

    @JsonBackReference
    @ManyToOne
    @JoinColumn (name = "keyword_id")
    private Keyword keyword;
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "url")
    private Set<PhoneNumber> phoneNumberSet = new HashSet<>();
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "url")
    private Set<Email> emailSet = new HashSet<>();

    public Url(String url) {
        this.url = url;
    }

    @Builder
    public Url(UUID id, String url, String title, int numberOfChildUrl, Keyword keyword, Set<PhoneNumber> phoneNumberSet, Set<Email> emailSet) {
        super(id);
        this.url = url;
        this.title = title;
        this.numberOfChildUrl = numberOfChildUrl;
        this.keyword = keyword;
        this.phoneNumberSet = phoneNumberSet;
        this.emailSet = emailSet;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumberOfChildUrl() {
        return numberOfChildUrl;
    }

    public void setNumberOfChildUrl(int numberOfChildUrl) {
        this.numberOfChildUrl = numberOfChildUrl;
    }

    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public boolean isFoundEmail() {
        return foundEmail;
    }

    public void setFoundEmail(boolean foundEmail) {
        this.foundEmail = foundEmail;
    }

    public boolean isFoundPhoneNumber() {
        return foundPhoneNumber;
    }

    public void setFoundPhoneNumber(boolean foundPhoneNumber) {
        this.foundPhoneNumber = foundPhoneNumber;
    }

    public String getUrlError() {
        return urlError;
    }

    public void setUrlError(String urlError) {
        this.urlError = urlError;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }

    public Set<PhoneNumber> getPhoneNumberSet() {
        return phoneNumberSet;
    }

    public void setPhoneNumberSet(Set<PhoneNumber> phoneNumberSet) {
        this.phoneNumberSet = phoneNumberSet;
    }

    public Set<Email> getEmailSet() {
        return emailSet;
    }

    public void setEmailSet(Set<Email> emailSet) {
        this.emailSet = emailSet;
    }
}
