package io.github.hefrankeleyn.hefconfig.client.service.impl;

import io.github.hefrankeleyn.hefconfig.client.conf.ChangeEvent;
import io.github.hefrankeleyn.hefconfig.client.service.HefConfigService;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
        Set<String> diffKeys = getUpdateKeys(this.config, changeEvent.getConfigs());
        if (diffKeys.isEmpty()) {
            return;
        }
        this.config = changeEvent.getConfigs();
        if (Objects.nonNull(this.config) && !this.config.isEmpty()) {
            System.out.println("====> [hefconfig] fire update config keys:" + diffKeys);
            applicationContext.publishEvent(new EnvironmentChangeEvent(diffKeys));
        }
    }

    private Set<String> getUpdateKeys(Map<String, String> oldConfig, Map<String, String> newConfigs) {
        Set<String> diffKeys = new HashSet<>();
        if (mapIsEmpty(oldConfig) && mapIsEmpty(newConfigs)) {
            return diffKeys;
        } else if (mapIsEmpty(oldConfig) && !mapIsEmpty(newConfigs)) {
            return newConfigs.keySet();
        } else if (!mapIsEmpty(oldConfig) && mapIsEmpty(newConfigs)) {
            return oldConfig.keySet();
        }
        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(oldConfig.keySet());
        allKeys.addAll(newConfigs.keySet());
        for (String oneKey : allKeys) {
            if (!(oldConfig.containsKey(oneKey) && newConfigs.containsKey(oneKey))) {
                diffKeys.add(oneKey);
            } else if (!oldConfig.get(oneKey).equals(newConfigs.get(oneKey))) {
                diffKeys.add(oneKey);
            }
        }
        return diffKeys;
    }

    private boolean mapIsEmpty(Map<String, String> map) {
        return map == null || map.isEmpty();
    }
}
