package com.webcrawler.webcrawlerapp.io;

import com.webcrawler.webcrawlerapp.domain.Keyword;
import com.webcrawler.webcrawlerapp.domain.Url;
import com.webcrawler.webcrawlerapp.io.detailScraper.Filter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchEngineService extends WebDocument {


    public SearchEngineService(String url) {
        super(url);
    }

    public Set<Url> getRootDomainUrlSet(Keyword crawlKeyword) {

        // get tag.a elements list
        List<String> ahrefList = getElementList(Tag.HYPER_LINK);

        // get attribute.href content
        Set<String> ahrefSet = ahrefList.stream()
                .filter(e -> containAttribute(Attribute.HREF, e))
                .map(e -> getAttributeContent(Attribute.HREF, e))
                .collect(Collectors.toSet());

        // custom clean up to root domain
        ahrefSet.removeIf(e -> Stream.of(Filter.getFilterList()).anyMatch(filter -> e.toLowerCase().contains(filter)));

        return formatToRootDomainHttp(ahrefSet).stream()
                .filter(e -> e.contains("https://"))
                .map(e -> {
                    Url url = new Url();
                    url.setKeyword(crawlKeyword);
                    url.setTitle(getDomainName(e));
                    url.setUrl(e);
                    return url;
                })
                .collect(Collectors.toSet());
    }

    private String getDomainName(String url) {
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

    private Set<String> formatToRootDomainHttp(Set<String> urls) {
        String startHttp = "https://";
        String endHttp = "/";
        Set<String> urlSet = urls.stream()
                .filter(e -> e.contains(startHttp))
                .map(e -> {
                    int startIndex = e.indexOf(startHttp);
                    int endIndex = e.indexOf(endHttp, startIndex + startHttp.length());

                    String newUrl;
                    if (endIndex < 0 || endIndex < startIndex) {
                        newUrl = e.substring(startIndex);
                    } else {
                        newUrl = e.substring(startIndex, endIndex);
                    }
                    return newUrl;
                })
                .collect(Collectors.toSet());
        return urlSet;
    }
}
