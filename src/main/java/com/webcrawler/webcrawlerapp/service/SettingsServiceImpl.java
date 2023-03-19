package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.domain.Settings;
import com.webcrawler.webcrawlerapp.repository.SettingsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SettingsServiceImpl implements SettingsService {

    private final SettingsRepository settingsRepository;

    public SettingsServiceImpl(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @Override
    public Settings updateSetting(Settings settings) {
        Settings updateSetting = getSetting();
                    updateSetting.setNumberOfPages(settings.getNumberOfPages());
                    updateSetting.setBingSearch(settings.isBingSearch());
                    updateSetting.setYahooSearch(settings.isYahooSearch());
                    updateSetting.setGoogleSearch(settings.isGoogleSearch());
                    updateSetting.setScrapeEmail(settings.isScrapeEmail());
                    updateSetting.setScrapePhoneNumber(settings.isScrapePhoneNumber());

            return settingsRepository.save(updateSetting);
    }

    @Override
    public Settings getSetting() {
        return settingsRepository.findAll().get(0);
    }

    private List<Settings> getAllSettings() {
        return settingsRepository.findAll();
    }
}
