package io.github.hefrankeleyn.hefconfig.client.repository;

import io.github.hefrankeleyn.hefconfig.client.beans.ConfigMetas;
import io.github.hefrankeleyn.hefconfig.client.repository.impl.HefRepositoryImpl;

import java.util.Map;

/**
 * interface to get config from remote
 */
public interface HefRepository {

    static HefRepository getDefault(ConfigMetas configMetas) {
        return new HefRepositoryImpl(configMetas);
    }

    Map<String, String> getConfig();
}

