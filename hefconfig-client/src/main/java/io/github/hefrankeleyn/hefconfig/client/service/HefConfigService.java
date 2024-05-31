package io.github.hefrankeleyn.hefconfig.client.service;

import io.github.hefrankeleyn.hefconfig.client.beans.ConfigMetas;
import io.github.hefrankeleyn.hefconfig.client.repository.HefRepository;
import io.github.hefrankeleyn.hefconfig.client.service.impl.HefConfigServiceImpl;

import java.util.Map;

public interface HefConfigService {

    static HefConfigService getDefault(ConfigMetas configMetas) {
        HefRepository defaultHefRepository = HefRepository.getDefault(configMetas);
        Map<String, String> config = defaultHefRepository.getConfig();
        return new HefConfigServiceImpl(config);
    }
    String[] getPropertyNames();
    String getProperty(String name);
}
