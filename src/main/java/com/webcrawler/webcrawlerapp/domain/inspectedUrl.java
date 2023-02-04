package com.webcrawler.webcrawlerapp.domain;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@Entity
public class inspectedUrl extends BaseEntity{

    private String address;
    private String title;
    private Set<String> urlSet;
    private String owner;
    private String phoneNumer;
    private String email;

}
