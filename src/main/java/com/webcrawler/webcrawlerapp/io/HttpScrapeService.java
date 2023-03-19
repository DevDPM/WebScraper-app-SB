package com.webcrawler.webcrawlerapp.io;

import com.webcrawler.webcrawlerapp.domain.*;
import com.webcrawler.webcrawlerapp.io.detailScraper.Detail;
import com.webcrawler.webcrawlerapp.io.detailScraper.Filter;
import com.webcrawler.webcrawlerapp.io.detailScraper.ScrapeDetail;
import com.webcrawler.webcrawlerapp.io.detailScraper.ScrapeDetailFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HttpScrapeService extends WebDocument {
    private final String CONTACT_PAGE = "contact";
    private Url rootUrl;

    public HttpScrapeService(Url url) {
        super(url.getUrl());
        setRootUrl(url);
    }

    public List<String> scrapeChildUrlList() {
        // get tag.a elements list
        List<String> ahrefElementList = getElementList(Tag.HYPER_LINK);

        // get attribute.href content
        Set<String> ahrefList = ahrefElementList.stream()
                .filter(e -> containAttribute(Attribute.HREF, e))
                .map(e -> getAttributeContent(Attribute.HREF, e))
                .collect(Collectors.toSet());

        ahrefList.removeIf(e -> Stream.of(Filter.getFilterList()).anyMatch(filter -> e.toLowerCase().contains(filter)));
        Set<String> formatUrlSet = formatUrl(ahrefList);
        List<String> childUrlList = new ArrayList<>(formatUrlSet.stream().toList());
        childUrlList.add(0, this.rootUrl.getUrl());

        // get contact page to first place
        childUrlList.stream()
                .sorted((s1, s2) -> s1.contains(CONTACT_PAGE) ? -1 : s2.contains(CONTACT_PAGE) ? 1 : 0);

        return childUrlList;
    }

    private Set<String> formatUrl(Set<String> urls) {
        String domainName = getDomainName();

        Set<String> cleanUrls = urls.stream().map(url -> {
                    if (Objects.equals(url, "") || (url.contains("=") || url.contains("#")))
                        return null;

                    if (url.contains(domainName)) {
                        if (url.startsWith("https://"))
                            return url;

                        if (url.contains("www.")) {
                            url = url.substring(url.indexOf("www."));
                            return "https://" + url;
                        }
                    }

                    if (url.charAt(0) == '/') {
                        if (url.length() > 1) {
                            if (url.charAt(1) == '/') {
                                if (url.contains("https://")) {
                                    return url.substring("//".length());
                                } else {
                                    url = url.substring("//".length());
                                    return "https://" + url;
                                }
                            }
                        }
                        return rootUrl.getUrl() + url;
                    }
                    return null;
                })
                .collect(Collectors.toSet());
        return cleanUrls.stream().filter(Objects::nonNull).collect(Collectors.toSet());
    }

    private String getDomainName() {
        String http = "https://";
        String www = "www.";

        String url = this.rootUrl.getUrl();
        int startDomain = url.indexOf(http);
        if (url.contains(www)) {
            startDomain = url.indexOf(www) + www.length();
        } else {
            startDomain += http.length();
        }

        int endDomain = url.indexOf('.', startDomain + 1);

        return url.substring(startDomain, endDomain);
    }

    private void setRootUrl(Url rootUrl) {
        this.rootUrl = rootUrl;
    }
}
