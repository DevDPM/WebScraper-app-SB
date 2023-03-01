package com.webcrawler.webcrawlerapp.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"url"})
@Entity
public class Email extends BaseEntity{

    private String email;

    @JsonBackReference
    @ManyToOne
    @JoinColumn (name = "url_id")
    private Url url;

    @Builder
    public Email(UUID id, String email, Url url) {
        super(id);
        this.email = email;
        this.url = url;
    }
}
