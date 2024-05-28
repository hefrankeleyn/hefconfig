package io.github.hefrankeleyn.hefconfig.client.service.impl;

import io.github.hefrankeleyn.hefconfig.client.HefPropertySource;
import io.github.hefrankeleyn.hefconfig.client.service.HefConfigService;

import java.util.HashMap;
import java.util.Map;

/**
 * @Date 2024/5/28
 * @Author lifei
 */
public class HefConfigServiceImpl implements HefConfigService {

    private final Map<String, String> config;

    public HefConfigServiceImpl(Map<String, String> config) {
        this.config = config;
    }

    @Override
    public String[] getPropertyNames() {
        return config.keySet().toArray(new String[0]);
    }

    @Override
    public String getProperty(String name) {
        return config.get(name);
    }
}
