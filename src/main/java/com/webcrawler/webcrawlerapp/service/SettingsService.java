package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.domain.Settings;

public interface SettingsService {
    Settings updateSetting(Settings settings);

    Settings getSetting();
}
