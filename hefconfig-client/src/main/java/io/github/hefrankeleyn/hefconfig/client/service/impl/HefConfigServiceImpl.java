package io.github.hefrankeleyn.hefconfig.client.service.impl;

import io.github.hefrankeleyn.hefconfig.client.conf.ChangeEvent;
import io.github.hefrankeleyn.hefconfig.client.service.HefConfigService;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Objects;

/**
 * @Date 2024/5/28
 * @Author lifei
 */
public class HefConfigServiceImpl implements HefConfigService {

    private Map<String, String> config;
    private ApplicationContext applicationContext;

    public HefConfigServiceImpl(ApplicationContext applicationContext, Map<String, String> config) {
        this.applicationContext = applicationContext;
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

    @Override
    public void onChange(ChangeEvent changeEvent) {
        this.config = changeEvent.getConfigs();
        if (Objects.nonNull(this.config) && !this.config.isEmpty()) {
            applicationContext.publishEvent(new EnvironmentChangeEvent(this.config.keySet()));
        }
    }
}
