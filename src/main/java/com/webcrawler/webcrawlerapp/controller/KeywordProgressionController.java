package com.webcrawler.webcrawlerapp.controller;

import com.webcrawler.webcrawlerapp.domain.KeywordProgression;
import com.webcrawler.webcrawlerapp.service.KeywordProgressionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200") // Angular frontend
@AllArgsConstructor
@RestController
public class KeywordProgressionController {

    KeywordProgressionService keywordProgressionService;

    @GetMapping(value = URL_PATH.API_GET_KEYWORD_PROGRESSION)
    public ResponseEntity<List<KeywordProgression>> getKeywordLoader() {

        List<KeywordProgression> progressList = new ArrayList<>(keywordProgressionService.GetAllKeywordProgression());

        return new ResponseEntity<>(progressList, HttpStatus.OK);
    }

    @DeleteMapping(value = URL_PATH.API_START_DELETION_GET_KEYWORD_PROGRESSION+"/{id}")
    public ResponseEntity deleteKeywordById(@PathVariable("id") UUID keywordProgressionId) {
        keywordProgressionService.deleteKeywordProgressionById(keywordProgressionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
