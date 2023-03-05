package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.domain.Settings;
import com.webcrawler.webcrawlerapp.repository.SettingsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SettingServiceImpl implements SettingService {

    private final SettingsRepository settingsRepository;

    public SettingServiceImpl(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @Override
    public Settings updateSetting(Settings settings) {
        Optional<Settings> updatedSetting = getAllSettings()
                .stream()
                .findFirst()
//                .filter(targetSetting -> { targetSetting
//                    return targetSetting.getId().equals(settings.getId());
//                })
                .map(updateSetting -> {
                    updateSetting.setNumberOfPages(settings.getNumberOfPages());
                    updateSetting.setBingSearch(settings.isBingSearch());
                    updateSetting.setGoogleSearch(settings.isGoogleSearch());
                    return updateSetting;
                });

        return updatedSetting.map(saveSetting -> {
            return settingsRepository.save(saveSetting);
        }).orElse(null);
    }

    @Override
    public Settings getSetting() {
        return settingsRepository.findAll().get(0);
    }

    private List<Settings> getAllSettings() {
        return settingsRepository.findAll();
    }
}
