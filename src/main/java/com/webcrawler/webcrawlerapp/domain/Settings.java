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

    private boolean googleSearch = true;
    private boolean bingSearch = true;
    private boolean yahooSearch = true;
    private boolean scrapeEmail = true;
    private boolean scrapePhoneNumber = true;
    private int numberOfPages = 10;

}
