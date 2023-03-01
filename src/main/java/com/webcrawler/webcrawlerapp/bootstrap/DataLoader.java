package com.webcrawler.webcrawlerapp.bootstrap;

import com.webcrawler.webcrawlerapp.domain.Email;
import com.webcrawler.webcrawlerapp.domain.Keyword;
import com.webcrawler.webcrawlerapp.domain.PhoneNumber;
import com.webcrawler.webcrawlerapp.domain.Url;
import com.webcrawler.webcrawlerapp.repository.KeywordRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

@Component
public class DataLoader implements CommandLineRunner {

    KeywordRepository keywordRepository;

    public DataLoader(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        dataToLoad();
    }

    private void dataToLoad() {

        Keyword keyword = new Keyword();
        keyword.setKeyword("stukadoor");

        Url url1 = new Url();
        url1.setKeyword(keyword);
        url1.setParentUrl("https://www.stukadoor.nl");
        url1.setNumberOfChildUrl("11");
        url1.setTitle("example");

        PhoneNumber phoneNumber11 = new PhoneNumber();
        phoneNumber11.setPhoneNumber("0654377127");
        phoneNumber11.setNumberOfHits(15);
        phoneNumber11.setUrl(url1);

        PhoneNumber phoneNumber12 = new PhoneNumber();
        phoneNumber12.setPhoneNumber("0612345678");
        phoneNumber12.setNumberOfHits(2);
        phoneNumber12.setUrl(url1);

        Email email11 = new Email();
        email11.setEmail("example1@gmail.com");
        email11.setUrl(url1);

        Email email12 = new Email();
        email12.setEmail("example2@gmail.com");
        email12.setUrl(url1);

        url1.getEmail().add(email11);
        url1.getEmail().add(email12);
        url1.getPhoneNumber().add(phoneNumber11);
        url1.getPhoneNumber().add(phoneNumber12);
        keyword.getUrls().add(url1);

        //

        Url url2 = new Url();
        url2.setKeyword(keyword);
        url2.setParentUrl("https://www.example.nl");
        url2.setNumberOfChildUrl("150");
        url2.setTitle("exampleee");

        PhoneNumber phoneNumber21 = new PhoneNumber();
        phoneNumber21.setPhoneNumber("0612345678");
        phoneNumber21.setNumberOfHits(2);
        phoneNumber21.setUrl(url2);

        PhoneNumber phoneNumber22 = new PhoneNumber();
        phoneNumber22.setPhoneNumber("0642390424");
        phoneNumber22.setNumberOfHits(99);
        phoneNumber22.setUrl(url2);

        Email email21 = new Email();
        email21.setEmail("example1@gmail.com");
        email21.setUrl(url2);

        Email email22 = new Email();
        email22.setEmail("example2@gmail.com");
        email22.setUrl(url2);


        url2.getEmail().add(email21);
        url2.getEmail().add(email22);
        url2.getPhoneNumber().add(phoneNumber21);
        url2.getPhoneNumber().add(phoneNumber22);
        keyword.getUrls().add(url2);

        keywordRepository.save(keyword);
        System.out.println("Test Data loaded");

    }
}
