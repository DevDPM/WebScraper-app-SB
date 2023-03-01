package com.webcrawler.webcrawlerapp.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"keyword"})
@AllArgsConstructor
public class Url extends BaseEntity{

    private String parentUrl;
    private String title;
    private String numberOfChildUrl;

    @JsonBackReference
    @ManyToOne
    @JoinColumn (name = "keyword_id")
    private Keyword keyword;
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "url")
    private Set<PhoneNumber> phoneNumber = new HashSet<>();
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "url")
    private Set<Email> email = new HashSet<>();

    @Builder
    public Url(UUID id, String parentUrl, String title, String numberOfChildUrl, Keyword keyword, Set<PhoneNumber> phoneNumber, Set<Email> email) {
        super(id);
        this.parentUrl = parentUrl;
        this.title = title;
        this.numberOfChildUrl = numberOfChildUrl;
        this.keyword = keyword;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
