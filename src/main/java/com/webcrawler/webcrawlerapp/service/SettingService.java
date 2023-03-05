package com.webcrawler.webcrawlerapp.service;

import com.webcrawler.webcrawlerapp.domain.Settings;

public interface SettingService {
    Settings updateSetting(Settings settings);

    Settings getSetting();
}
