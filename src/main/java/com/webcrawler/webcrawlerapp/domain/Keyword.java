package com.webcrawler.webcrawlerapp.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Keyword extends BaseEntity{

    private String keyword;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "keyword")
    private List<Url> urls = new ArrayList<>();

    @Builder
    public Keyword(UUID id, String keyword, List<Url> urls) {
        super(id);
        this.keyword = keyword;
        this.urls = urls;
    }
}
