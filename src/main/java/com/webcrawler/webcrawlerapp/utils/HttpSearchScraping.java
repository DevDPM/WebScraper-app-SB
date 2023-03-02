package com.webcrawler.webcrawlerapp.utils;

import com.webcrawler.webcrawlerapp.domain.Email;
import com.webcrawler.webcrawlerapp.domain.PhoneNumber;
import com.webcrawler.webcrawlerapp.domain.Url;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.webcrawler.webcrawlerapp.utils.Element.*;

public class HttpSearchScraping {
    private boolean findEmail = false;
    private boolean findTelephone = false;
    private Map<String, Integer> emails = new HashMap<>();
    private Map<String, Integer> telephones = new HashMap<>();
    private Set<String> urlsToHttpCrawl = new HashSet<>();
    private List<Url> urlResultList = new ArrayList<>();


    private final String[] FILTERLIST = {
            "=", "google", "bing", "youtube", "facebook",
            "instagram", "twitter", "wikipedia", "roc",
            "indeed", "adidas", "perrysport", "intersport",
            "zalando", "decathlon", "amazon", "microsoft",
            "nu", "vacatures", "marktplaats", "pinterest",
            "linkedin", "trustpilot"};

    private final String[] TEL_NUMBER_FILTER = {
            "06", "+31", "020", "085", "0343", "0299"
    };

    public boolean setUrls(Set<String> urls) {
        if (urls == null || urls.isEmpty())
            return false;

        this.urlsToHttpCrawl = urls;
        return true;
    }


    public List<Url> start() {

        Set<String> parentUrls = getUrlsToHttpCrawl();
        if (parentUrls == null || parentUrls.isEmpty()) {
            throw new RuntimeException("No urls to crawl");
        }

        Set<String> filterParentUrls = filterUrlSet(parentUrls);

        // crawl each parentUrl
        for (String parentUrl : filterParentUrls) {

            Url url = new Url();

            url.setParentUrl(parentUrl);

            WebDocument document = new WebDocument();
            try {
                document.crawlUrl(parentUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Element.setWebDocument(document);

            // find all childUrls in parentUrl
            List<String> foundChildUrls = getElementListFromElementHTML(Tag.HYPER_LINK, Tag.HTML)
                    .stream()
                    .filter(e -> containAttribute(Attribute.HREF, e))
                    .map(e -> {
                        return getAttributeContent(Attribute.HREF, e);
                    }).collect(Collectors.toList());
            foundChildUrls.add(parentUrl);

            // remove duplicates from list
            Set<String> removeDuplicateUrlsSet = new HashSet<>(foundChildUrls);
            foundChildUrls.clear();
            foundChildUrls.addAll(filterUrlSet(removeDuplicateUrlsSet));

            url.setNumberOfChildUrl(String.valueOf(foundChildUrls.size()));

            // reformat childUrls to complete url, simultaneously remove none urls and add none urls to specified set
            // none urls may contain targets to scrape i.e. <a href="mailto:info@example.nl>
            final String DOMAIN_NAME = subtractDomainName(parentUrl);
            url.setTitle(DOMAIN_NAME);

            List<String> noneHttpUrls = new ArrayList<>();
            foundChildUrls = foundChildUrls
                    .stream()
                    .map(e -> {
                        if (e.contains(DOMAIN_NAME)) {
                            if (e.contains("https://"))
                                return e;
                            if (e.charAt(0) == '/') {
                                if (e.contains("www.")) {
                                    e = e.substring(e.indexOf("www."));
                                    return "https://" + e;
                                } else if (e.charAt(0) == '/' && e.charAt(1) == '/') {
                                    e = e.substring("//".length());
                                    return "https://" + e;
                                } else {
                                    return parentUrl + e;
                                }
                            }
                        }
                        // none http/www added to specified set and return null to list;
                        noneHttpUrls.add(e);
                        return null;
                    })
                    .collect(Collectors.toList());
            foundChildUrls.removeIf(Objects::isNull);

            // scrape for info in noneHttpUrls
            if (!noneHttpUrls.isEmpty())
                startScrapeDetails(truncateListToString(noneHttpUrls));

            // search contact in urls and put it on first priority in list for crawling
            final String prioritizeUrlContainsWord = "contact";
            List<String> organizedChildUrls = prioritizeUrlListByTitle(foundChildUrls, prioritizeUrlContainsWord);

            // crawl through childUrls and scrape activated targets
            for (String childUrl : organizedChildUrls) {
                WebDocument targetDocument = new WebDocument();
                try {
                    targetDocument.crawlUrl(childUrl);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Element.setWebDocument(targetDocument);
                startScrapeDetails();
            }

            Url completeUrl = addScrapeResultsToUrl(url);
            getUrlResultList().add(completeUrl);

            getEmails().clear();
            getTelephones().clear();
        }

        return urlResultList;
    }

    private Url addScrapeResultsToUrl(Url url) {

        Set<PhoneNumber> phoneNumberSet = new HashSet<>();
        if (!getTelephones().isEmpty()) {
            for (Map.Entry<String, Integer> singlePhoneNumber : getTelephones().entrySet()) {
                PhoneNumber phoneNumber = new PhoneNumber();
                phoneNumber.setUrl(url);
                phoneNumber.setPhoneNumber(singlePhoneNumber.getKey());
                phoneNumber.setNumberOfHits(singlePhoneNumber.getValue().toString());

                phoneNumberSet.add(phoneNumber);
            }
        }

        Set<Email> emailSet = new HashSet<>();
        if (!getEmails().isEmpty()) {
            for (Map.Entry<String, Integer> singleEmail : getEmails().entrySet()) {
                Email email = new Email();
                email.setUrl(url);
                email.setEmail(singleEmail.getKey());
                email.setNumberOfHits(singleEmail.getValue().toString());

                emailSet.add(email);
            }
        }

        url.getPhoneNumberSet().addAll(phoneNumberSet);
        url.getEmailSet().addAll(emailSet);

        return url;
    }


    private String truncateListToString(List<String> noneHttpUrls) {
        StringBuilder truncateHTML = new StringBuilder();
        for (String noneHttpUrl : noneHttpUrls) {
            truncateHTML.append(noneHttpUrl);
        }
        return truncateHTML.toString();
    }

    private void startScrapeDetails() {
        // todo
        // very sketchy, but full body subtracted works best for telephone and full html for email
        String contentHTML = Element.getContentAndSubtractAllElements(Tag.BODY);
        setFindEmail(false);
        startScrapeDetails(contentHTML);
        setFindEmail(true);

        contentHTML = truncateListToString(Element.getElementList(Tag.HTML));
        setFindTelephone(false);
        startScrapeDetails(contentHTML);
        setFindTelephone(true);
    }

    private void startScrapeDetails(String truncatedHTML) {
        if (isFindEmail())
            scrapeEmail(truncatedHTML);
        if (isFindTelephone())
            scrapeTelephone(truncatedHTML);
    }

    private void scrapeTelephone(String truncatedHTML) {
        String spacelessHTML = truncatedHTML.replaceAll("\\s+", "");

        for (String telFilter : TEL_NUMBER_FILTER) {
            int startIndex = 0;
            while(spacelessHTML.contains(telFilter)) {

                startIndex = spacelessHTML.indexOf(telFilter);
                int endIndex = startIndex+25;

                if (endIndex > spacelessHTML.length())
                    endIndex = spacelessHTML.length();


                String foundString = spacelessHTML.substring(startIndex, endIndex);
                String numberOnly = foundString.replaceAll("[^0-9]", "");
                numberOnly = numberOnly.replaceAll("31", "0");

                if (numberOnly.length() == 10) {
                    getTelephones().computeIfPresent(numberOnly, (k, v) -> v + 1);
                    getTelephones().putIfAbsent(numberOnly, 1);
                }

                startIndex = endIndex;
                spacelessHTML = spacelessHTML.substring(startIndex);
            }


        }
    }

    private void scrapeEmail(String truncatedHTML) {
        Matcher m = Pattern.compile(
                "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.(?:nl|com|be)|"
        ).matcher(truncatedHTML);
        while (m.find()) {
            if (!Objects.equals(m.group(), "")) {
                String foundEmail = m.group();
                getEmails().computeIfPresent(foundEmail, (k, v) -> v + 1);
                getEmails().putIfAbsent(foundEmail, 1);
            }
        }
    }

    private List<String> prioritizeUrlListByTitle(List<String> foundUrls, String word) {
        List<String> urlList = new ArrayList<>(foundUrls);
        List<String> filteredContacts = urlList
                .stream()
                .filter(e -> e.contains(word))
                .toList();

        if (filteredContacts.size() > 0) {
            int i = 0;
            for (String filteredContact : filteredContacts) {
                int indexOfFilteredContact = urlList.indexOf(filteredContact);
                Collections.swap(urlList, i, indexOfFilteredContact);
                i++;
            }
        }
        return urlList;
    }

    private Set<String> filterUrlSet(Set<String> urlsToCrawl) {
        Set<String> filteredList = urlsToCrawl;
        for (String filter : FILTERLIST) {
            filteredList.removeIf(e -> e.contains(filter));
        }
        return filteredList;
    }

    private String subtractDomainName(String url) {

        String http = "https://";
        String www = "www.";

        int startDomain = url.indexOf(http);
        if (url.contains(www)) {
            startDomain = url.indexOf(www) + www.length();
        } else {
            startDomain += http.length();
        }

        int endDomain = url.indexOf('.', startDomain + 1);


        return url.substring(startDomain, endDomain);
    }

    public List<Url> getUrlResultList() {
        return urlResultList;
    }

    public void setUrlResultList(List<Url> urlResultList) {
        this.urlResultList = urlResultList;
    }

    public Set<String> getUrlsToHttpCrawl() {
        return urlsToHttpCrawl;
    }

    private boolean isFindEmail() {
        return findEmail;
    }

    public void setFindEmail(boolean findEmail) {
        this.findEmail = findEmail;
    }

    private boolean isFindTelephone() {
        return findTelephone;
    }

    public void setFindTelephone(boolean findTelephone) {
        this.findTelephone = findTelephone;
    }

    public Map<String, Integer> getEmails() {
        return emails;
    }

    public Map<String, Integer> getTelephones() {
        return telephones;
    }
}
