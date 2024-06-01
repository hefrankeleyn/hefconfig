package io.github.hefrankeleyn.hefconfig.client.conf;

import io.github.hefrankeleyn.hefconfig.client.beans.ConfigMetas;

import java.util.Map;

/**
 * @Date 2024/6/1
 * @Author lifei
 */
public class ChangeEvent {

    private ConfigMetas configMetas;
    private Map<String, String> configs;

    public ChangeEvent(ConfigMetas configMetas, Map<String, String> configs) {
        this.configMetas = configMetas;
        this.configs = configs;
    }

    public ConfigMetas getConfigMetas() {
        return configMetas;
    }

    public void setConfigMetas(ConfigMetas configMetas) {
        this.configMetas = configMetas;
    }

    public Map<String, String> getConfigs() {
        return configs;
    }

    public void setConfigs(Map<String, String> configs) {
        this.configs = configs;
    }
}
