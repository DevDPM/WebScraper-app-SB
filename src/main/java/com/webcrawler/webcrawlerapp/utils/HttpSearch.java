package com.webcrawler.webcrawlerapp.utils;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.webcrawler.webcrawlerapp.utils.Element.*;

public class HttpSearch {
    private boolean findEmail;
    private boolean findTelephone;
    private Set<String> emails;
    private Map<String, Integer> telephones;
    private Set<String> urlSet;
    private Set<String> urlChilds;


    // for result display purpose
    private List<String> resultUrls;
    private List<Set<String>> resultEmails;
    private List<Map<String, Integer>> resultTelephones;

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

    public HttpSearch() {
        this.findEmail = false;
        this.findTelephone = false;
        this.emails = new HashSet<>();
        this.telephones = new HashMap<>();
        this.urlSet = new HashSet<>();
        this.urlChilds = new HashSet<>();
        this.resultUrls = new ArrayList<>();
        this.resultEmails = new ArrayList<>();
        this.resultTelephones = new ArrayList<>();
    }

    public boolean setUrls(Set<String> urls) {
        if (urls == null || urls.isEmpty())
            return false;

        this.urlSet = urls;
        return true;
    }

    public boolean start() throws IOException {


        Set<String> parentUrls = getUrlSet();
        if (parentUrls == null || parentUrls.isEmpty()) {
            throw new RuntimeException("No urls to crawl");
        }

        // filter url's by filterList (i.e. google, bing, youtube are unnecessary targets
        Set<String> filterParentUrls = filterUrlSet(parentUrls);

        System.out.println("Total unique links found: " + filterParentUrls.size());

        // crawl each parentUrl
        for (String parentUrl : filterParentUrls) {
            System.out.println("Start crawling: " + parentUrl);

            WebDocument document = new WebDocument();
            document.crawlUrl(parentUrl);
            Element.setWebDocument(document);

//            System.out.println(document.getHTML());
//            Element.getElementList(Tag.BODY).forEach(System.out::println);
//            System.out.println(getContentAndSubtractAllElements(Tag.HTML));
//            getElementListFromElementHTML(Tag.HYPER_LINK, Tag.HTML).forEach(System.out::println);

            // find all childUrls in parentUrl
            List<String> foundChildUrls = getElementListFromElementHTML(Tag.HYPER_LINK, Tag.HTML)
                    .stream()
                    .filter(e -> containAttribute(Attribute.HREF, e))
                    .map(e -> {
                        return getAttributeContent(Attribute.HREF, e);
                    }).collect(Collectors.toList());
            foundChildUrls.add(parentUrl);

//            foundChildUrls.forEach(System.out::println);

            // remove duplicates from list
            Set<String> removeDuplicateUrlsSet = new HashSet<>(foundChildUrls);
            foundChildUrls.clear();
            foundChildUrls.addAll(filterUrlSet(removeDuplicateUrlsSet));

            // reformat childUrls to complete url, simultaneously remove none urls and add none urls to specified set
            // none urls may contain targets to scrape i.e. <a href="mailto:info@example.nl>
            final String DOMAIN_NAME = subtractDomainName(parentUrl);
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

//            noneHttpUrls.forEach(System.out::println);

            // search contact in urls and put it on first priority in list for crawling
            final String prioritizeUrlContainsWord = "contact";
            List<String> organizedChildUrls = prioritizeUrlListByTitle(foundChildUrls, prioritizeUrlContainsWord);

            System.out.println("Child links found: " + organizedChildUrls.size());
            System.out.println("crawling through child links");
            // crawl through childUrls and scrape activated targets
            for (String childUrl : organizedChildUrls) {
                WebDocument targetDocument = new WebDocument();
                targetDocument.crawlUrl(childUrl);
                Element.setWebDocument(targetDocument);
                startScrapeDetails();
            }

            this.resultUrls.add(parentUrl);
            this.resultEmails.add(new HashSet<>(getEmails()));
            this.resultTelephones.add(new HashMap<>(getTelephones()));

            getEmails().clear();
            getTelephones().clear();
        }

        System.out.println("Done crawling");
        System.out.println("");
        System.out.println("Results: ");
        for (int i = 0; i < resultUrls.size(); i++) {
            System.out.println(resultUrls.get(i));
            resultEmails.get(i).forEach(e -> System.out.println("\t" + e));
            for (Map.Entry<String, Integer> result : resultTelephones.get(i).entrySet()) {
                System.out.println("\t\t" + result.getValue() + "x - " + result.getKey());
            }
        }

        return true;
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
            getEmails().add(m.group());
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

    public Set<String> getUrlSet() {
        return urlSet;
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

    public Set<String> getEmails() {
        return emails;
    }

    public Map<String, Integer> getTelephones() {
        return telephones;
    }
}
