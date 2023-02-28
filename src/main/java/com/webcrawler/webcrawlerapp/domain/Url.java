package com.webcrawler.webcrawlerapp.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Url extends BaseEntity{

    private String parentUrl;
    private String title;
    private String numberOfChildUrl;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "url")
    private Set<PhoneNumber> phoneNumber = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "url")
    private Set<Email> email = new HashSet<>();

}
