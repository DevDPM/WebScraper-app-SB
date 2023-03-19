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

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeywordDTO extends BaseEntity{

    private String keyword;
    private boolean googleSearch = true;
    private boolean bingSearch = true;
    private boolean yahooSearch = true;
    private boolean scrapeEmail = true;
    private boolean scrapePhoneNumber = true;
    private int numberOfPages = 10;

    private Set<UrlDTO> urls = new HashSet<>();

}
