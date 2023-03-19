package com.webcrawler.webcrawlerapp.bootstrap;

import com.webcrawler.webcrawlerapp.domain.*;
import com.webcrawler.webcrawlerapp.repository.SettingsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {


    private final SettingsRepository settingsRepository;

    public DataLoader(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        dataToLoad();
    }

    private void dataToLoad() {

        Settings settings = new Settings();
        settings.setGoogleSearch(true);
        settings.setBingSearch(true);
        settings.setYahooSearch(true);
        settings.setNumberOfPages(10);
        settingsRepository.save(settings);

        System.out.println("Settings loaded");

    }
}
