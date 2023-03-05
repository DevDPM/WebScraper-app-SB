package com.webcrawler.webcrawlerapp.domain;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Settings extends BaseEntity {

    private boolean googleSearch;
    private boolean bingSearch;
    private int numberOfPages;

}
