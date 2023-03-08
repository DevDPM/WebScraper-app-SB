package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.domain.Keyword;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.Callable;

public interface CrawlService extends Callable<ResponseEntity> {

    ResponseEntity call();

    void setFindNumberOfPages(int findNumberOfPages);
    void setBingSearch(boolean bingSearch);
    void setGoogleSearch(boolean googleSearch);

    void setKeyword(Keyword newKeyword);
    Keyword getKeyword();
}
