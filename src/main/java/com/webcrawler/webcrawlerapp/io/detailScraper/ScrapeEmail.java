package com.webcrawler.webcrawlerapp.io.detailScraper;

import com.webcrawler.webcrawlerapp.domain.*;
import com.webcrawler.webcrawlerapp.io.HttpScrapeService;
import com.webcrawler.webcrawlerapp.io.Tag;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ScrapeEmail implements ScrapeDetail<Url, HttpScrapeService> {

    private boolean reliableDetailFound = false;
    private Url url;

    @Override
    public synchronized void scrapeDetailsToUrl(Url url, HttpScrapeService httpScrapeService) {
        setUrl(url);

        if (url == null || httpScrapeService == null)
            return;

        httpScrapeService.getElementList(Tag.HYPER_LINK)
                .stream()
                .filter(Ahref -> Ahref.contains("mailto:"))
                .forEach(Ahref -> {
                    Map<String, Integer> emailHits = scrapeEmail(Ahref);

                    if (emailHits.isEmpty())
                        return;

                    addNewOrExistingEmail(url, emailHits, true);
                });

        if (reliableDetailFound) {
            return;
        }

        String fullHtmlPage = httpScrapeService.getHtmlDocument();
        if (fullHtmlPage == null) {
            System.out.println(this.getClass() + ": html is empty.");
            return;
        }

        Map<String, Integer> emailHits = scrapeEmail(fullHtmlPage);
        if (!emailHits.isEmpty())
            addNewOrExistingEmail(url, emailHits, false);
    }

    private void addNewOrExistingEmail(Url url, Map<String, Integer> emailHits, boolean trustworthy) {

        for (Map.Entry<String, Integer> emailHit : emailHits.entrySet()) {
            Optional<Email> emailOptional = url.getEmailSet()
                    .stream()
                    .filter(emailDTO -> emailDTO.getEmail().contains(emailHit.getKey()))
                    .findFirst();
            if (emailOptional.isPresent()) {
                Email presentEmail = emailOptional.get();
                int newNumberOfHits = presentEmail.getNumberOfHits() + emailHit.getValue();
                presentEmail.setNumberOfHits(newNumberOfHits);
                if (!presentEmail.isTrustworthy()) {
                    presentEmail.setTrustworthy(trustworthy);
                    setReliableDetailFound(trustworthy);
                }
            } else {
                Email email = new Email();
                email.setEmail(emailHit.getKey());
                email.setNumberOfHits(emailHit.getValue());
                email.setTrustworthy(trustworthy);
                email.setUrl(url);
                url.getEmailSet().add(email);
                setReliableDetailFound(trustworthy);
            }
        }
    }

    private Map<String, Integer> scrapeEmail(String html) {            /////////// make private
        Matcher m = Pattern.compile(
                "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.(?:nl|com|be|nu|eu)|"
        ).matcher(html);

        Map<String, Integer> foundEmailSet = new HashMap<>();

        while (m.find()) {
            if (!Objects.equals(m.group(), "")) {
                String foundEmail = m.group();
                foundEmailSet.computeIfPresent(foundEmail, (k, v) -> v + 1);
                foundEmailSet.putIfAbsent(foundEmail, 1);
            }
        }
        return foundEmailSet;
    }

    private void setReliableDetailFound(boolean reliableDetailFound) {
        this.reliableDetailFound = reliableDetailFound;
    }

    @Override
    public boolean foundReliableDetail() {
        return reliableDetailFound;
    }

    private void setUrl(Url url) {
        this.url = url;
    }
}
