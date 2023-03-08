package com.webcrawler.webcrawlerapp.controller;

import com.webcrawler.webcrawlerapp.domain.Settings;
import com.webcrawler.webcrawlerapp.service.SettingsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200") // Angular frontend
@AllArgsConstructor
@RestController
public class SettingController {

    private final SettingsService settingsService;

    @PostMapping(value = URL_PATH.API_SETTINGS)
    public ResponseEntity handlePost(@RequestBody Settings settings) {
        System.out.println("received: " + settings.toString());
        Settings updateSettings = settings;
        Settings newSetting = settingsService.updateSetting(updateSettings);

        return new ResponseEntity(newSetting, HttpStatus.OK);
    }

    @GetMapping(value = URL_PATH.API_SETTINGS)
    public ResponseEntity getSettings() {
        Settings onlySetting = settingsService.getSetting();

        return new ResponseEntity(onlySetting, HttpStatus.OK);
    }
}
