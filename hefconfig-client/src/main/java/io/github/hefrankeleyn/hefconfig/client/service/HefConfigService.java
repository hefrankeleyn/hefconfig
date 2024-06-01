package io.github.hefrankeleyn.hefconfig.client.service;

import io.github.hefrankeleyn.hefconfig.client.beans.ConfigMetas;
import io.github.hefrankeleyn.hefconfig.client.conf.ChangeListener;
import io.github.hefrankeleyn.hefconfig.client.repository.HefRepository;
import io.github.hefrankeleyn.hefconfig.client.service.impl.HefConfigServiceImpl;
import org.springframework.context.ApplicationContext;

import java.util.Map;

public interface HefConfigService extends ChangeListener {

    static HefConfigService getDefault(ApplicationContext applicationContext, ConfigMetas configMetas) {
        HefRepository defaultHefRepository = HefRepository.getDefault(configMetas);
        Map<String, String> config = defaultHefRepository.getConfig();
        HefConfigServiceImpl hefConfigService = new HefConfigServiceImpl(applicationContext, config);
        defaultHefRepository.addListener(hefConfigService);
        return hefConfigService;

    }
    String[] getPropertyNames();
    String getProperty(String name);
}
