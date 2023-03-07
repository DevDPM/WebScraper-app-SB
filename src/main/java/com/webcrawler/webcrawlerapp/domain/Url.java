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
    private PageHealth pageHealth;

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

    @Builder
    public Url(UUID id, String parentUrl, String title, String numberOfChildUrl, Keyword keyword, Set<PhoneNumber> phoneNumberSet, Set<Email> emailSet) {
        super(id);
        this.parentUrl = parentUrl;
        this.title = title;
        this.numberOfChildUrl = numberOfChildUrl;
        this.keyword = keyword;
        this.phoneNumberSet = phoneNumberSet;
        this.emailSet = emailSet;
    }
}
