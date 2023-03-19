package com.webcrawler.webcrawlerapp.domain;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlDTO extends BaseEntity{

    private String url;
    private String title;
    private String numberOfChildUrl;
    private Quality quality;
    private boolean foundEmail;
    private boolean foundPhoneNumber;
    private String urlError = "None";
    private Keyword keyword;
    private Set<PhoneNumberDTO> phoneNumberSet = new HashSet<>();
    private Set<EmailDTO> emailSet = new HashSet<>();

    public UrlDTO(String url) {
        this.url = url;
    }
}
