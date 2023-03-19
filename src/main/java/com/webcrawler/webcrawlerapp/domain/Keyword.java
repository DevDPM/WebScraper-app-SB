package com.webcrawler.webcrawlerapp.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Keyword extends BaseEntity{

    private String keyword;
    private boolean googleSearch = true;
    private boolean bingSearch = false;
    private boolean yahooSearch = false;
    private boolean scrapeEmail = false;
    private boolean scrapePhoneNumber = false;
    private int numberOfPages = 10;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "keyword")
    private Set<Url> urls = new HashSet<>();

    @Builder
    public Keyword(UUID id, String keyword, Set<Url> urls) {
        super(id);
        this.keyword = keyword;
        this.urls = urls;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public boolean isGoogleSearch() {
        return googleSearch;
    }

    public void setGoogleSearch(boolean googleSearch) {
        this.googleSearch = googleSearch;
    }

    public boolean isBingSearch() {
        return bingSearch;
    }

    public void setBingSearch(boolean bingSearch) {
        this.bingSearch = bingSearch;
    }

    public boolean isYahooSearch() {
        return yahooSearch;
    }

    public void setYahooSearch(boolean yahooSearch) {
        this.yahooSearch = yahooSearch;
    }

    public boolean isScrapeEmail() {
        return scrapeEmail;
    }

    public void setScrapeEmail(boolean scrapeEmail) {
        this.scrapeEmail = scrapeEmail;
    }

    public boolean isScrapePhoneNumber() {
        return scrapePhoneNumber;
    }

    public void setScrapePhoneNumber(boolean scrapePhoneNumber) {
        this.scrapePhoneNumber = scrapePhoneNumber;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public Set<Url> getUrls() {
        return urls;
    }

    public void setUrls(Set<Url> urls) {
        this.urls = urls;
    }

    public void removeUrl(Url url) {
        urls.remove(url);
    }
}
