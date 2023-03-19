package com.webcrawler.webcrawlerapp.io.detailScraper;

import com.webcrawler.webcrawlerapp.domain.*;
import com.webcrawler.webcrawlerapp.io.Attribute;
import com.webcrawler.webcrawlerapp.io.HttpScrapeService;
import com.webcrawler.webcrawlerapp.io.Tag;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ScrapePhoneNumber implements ScrapeDetail<Url, HttpScrapeService> {

    private final String[] TARGET_WORD = {"bel","tel", "nummer"};

    private final String[] VALID_PHONE_NUMBER = {
            "06", "010", "011", "013", "014", "015", "016", "017", "018", "020", "022", "023", "024", "025",
            "026", "029", "030", "031", "032", "033", "034", "035", "036", "038", "040", "041", "043", "044",
            "045", "046", "047", "048", "049", "050", "051", "052", "053", "054", "055", "056", "057", "058",
            "059", "070", "071", "072", "073", "074", "075", "076", "077", "078", "079", "08", "09"
    };

    private boolean reliableDetailFound = false;

    @Override
    public void scrapeDetailsToUrl(Url url, HttpScrapeService httpScrapeService) {

        if (url == null || httpScrapeService == null)
            return;

        httpScrapeService.getElementList(Attribute.HREF, Tag.HYPER_LINK)
                .stream()
                .filter(ahref -> ahref.contains("tel:"))
                .forEach(ahref -> {
            Map<String, Integer> phoneHits = scrapePhone(ahref);

            if (phoneHits.isEmpty())
                return;

            addNewOrExistingPhoneNumber(url, phoneHits, true);
        });

        if (reliableDetailFound) {
            return;
        }

        String fullHtmlPageNoneSpace = httpScrapeService.getHtmlDocument();
        if (fullHtmlPageNoneSpace == null) {
            System.out.println(this.getClass() + ": html is empty.");
            return;
        }

        Set<String> potentialPhoneNumbers = splitContentContainingPhoneNumber(fullHtmlPageNoneSpace);
        if (!potentialPhoneNumbers.isEmpty()) {
            potentialPhoneNumbers.forEach(content -> {
                Map<String, Integer> phoneHits = scrapePhone(content);

                if (phoneHits.isEmpty())
                    return;

                addNewOrExistingPhoneNumber(url, phoneHits, false);
                /////////////////////////////
                phoneHits = null;
            });
        }

        /////////////////////////
        fullHtmlPageNoneSpace = null;
        potentialPhoneNumbers = null;
    }

    private void addNewOrExistingPhoneNumber(Url url, Map<String, Integer> phoneHits, boolean trustworthy) {

        for (Map.Entry<String, Integer> phoneHit : phoneHits.entrySet()) {
            Optional<PhoneNumber> phoneNumberOptional = url.getPhoneNumberSet()
                    .stream()
                    .filter(phoneNumber -> phoneNumber.getPhoneNumber().contains(phoneHit.getKey()))
                    .findFirst();
            if (phoneNumberOptional.isPresent()) {
                PhoneNumber presentPhoneNumber = phoneNumberOptional.get();
                int newNumberOfHits = presentPhoneNumber.getNumberOfHits() + phoneHit.getValue();
                presentPhoneNumber.setNumberOfHits(newNumberOfHits);
                if (!presentPhoneNumber.isTrustworthy()) {
                    presentPhoneNumber.setTrustworthy(trustworthy);
                    setReliableDetailFound(trustworthy);
                }
            } else {
                PhoneNumber phoneNumber = new PhoneNumber();
                phoneNumber.setPhoneNumber(phoneHit.getKey());
                phoneNumber.setNumberOfHits(phoneHit.getValue());
                phoneNumber.setTrustworthy(trustworthy);
                phoneNumber.setUrl(url);
                url.getPhoneNumberSet().add(phoneNumber);
                setReliableDetailFound(trustworthy);
            }
        }
        /////////////////////////
        phoneHits = null;
    }

    private Map<String, Integer> scrapePhone(String html) {
        Map<String, Integer> foundPhoneNumberSet = new HashMap<>();

        html = html.replaceAll("\\s+", "")         // removes all white space
                .replaceAll("[-\\\\/]","")      // removes all \ and /
                .replaceAll("[/+]31", "0");     // replaces all +31 to 0

        if (html.contains("&#8211;"))
            html = html.replaceAll("&#8211;", "");

        Matcher m = Pattern.compile(
                "\\d{10}"
        ).matcher(html);

        while (m.find()) {
            if (!Objects.equals(m.group(), "")) {
                String foundPhoneNumber = m.group();
                for (String phoneNumber : VALID_PHONE_NUMBER) {
                    if (foundPhoneNumber.startsWith(phoneNumber)) {
                        foundPhoneNumberSet.computeIfPresent(foundPhoneNumber, (k, v) -> v + 1);
                        foundPhoneNumberSet.putIfAbsent(foundPhoneNumber, 1);
                        break;
                    }
                }
            }
        }
        html = null;
        return foundPhoneNumberSet;
    }

    private void setReliableDetailFound(boolean reliableDetailFound) {
        this.reliableDetailFound = reliableDetailFound;
    }

    @Override
    public boolean foundReliableDetail() {
        return false;
    }

    private Set<String> splitContentContainingPhoneNumber(String html) {
        Set<String> potentialPhoneNumbers = new HashSet<>();

        for (String WORD : TARGET_WORD) {
            String splitHtml = html.replaceAll("\\<.*?\\>", "") // deleted all between < /// >
                    .replaceAll("\\s+", "").toLowerCase();
            while (splitHtml.contains(WORD)) {
                int startIndex = 0;
                startIndex = splitHtml.indexOf(WORD, startIndex);
                int endIndex = startIndex + 100;
                endIndex = Math.min(endIndex, splitHtml.length());

                String potentialContent = splitHtml.substring(startIndex, endIndex);
                potentialPhoneNumbers.add(potentialContent);
                splitHtml = splitHtml.substring(endIndex);
            }
            splitHtml = null;
        }
        html = null;

        return potentialPhoneNumbers;
    }
}
