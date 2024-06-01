package io.github.hefrankeleyn.hefconfig.client.repository.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import io.github.hefrankeleyn.hefconfig.client.beans.ConfigMetas;
import io.github.hefrankeleyn.hefconfig.client.beans.Configs;
import io.github.hefrankeleyn.hefconfig.client.conf.ChangeEvent;
import io.github.hefrankeleyn.hefconfig.client.conf.ChangeListener;
import io.github.hefrankeleyn.hefconfig.client.repository.HefRepository;
import io.github.hefrankeleyn.utils.http.HttpInvoker;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Date 2024/5/31
 * @Author lifei
 */
public class HefRepositoryImpl implements HefRepository {

    private final ConfigMetas configMetas;

    private final ScheduledExecutorService executor;
    private Map<String, Long> VERSIONS = Maps.newHashMap();
    private Map<String, Map<String, String>> CONFIGS = Maps.newHashMap();

    private List<ChangeListener> listenerList = Lists.newArrayList();

    public void addListener(ChangeListener listener) {
        listenerList.add(listener);
    }


    public HefRepositoryImpl(ConfigMetas configMetas) {
        this.configMetas = configMetas;
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(this::hearbeat, 1000, 5000, TimeUnit.MILLISECONDS);
    }

    @Override
    public Map<String, String> getConfig() {
        String key = configMetas.genKey();
        if (CONFIGS.containsKey(key)) {
            return CONFIGS.get(key);
        }
        return findAllConfigs();
    }

    private Map<String, String> findAllConfigs() {
        System.out.println("====> [hefconfig] find all configs from hefconfig-server");
        String url = configMetas.listPath();
        TypeToken<List<Configs>> typeToken = new TypeToken<>() {};
        List<Configs> configs = HttpInvoker.httpGet(url, typeToken);
        Map<String, String> result = new HashMap<>();
        if (Objects.nonNull(configs) && !configs.isEmpty()) {
            for (Configs config : configs) {
                result.put(config.getCkey(), config.getCvalue());
            }
        }
        return result;
    }

    private void hearbeat() {
        String url = configMetas.versionPath();
        Long newVersion = HttpInvoker.httpGet(url, Long.class);
        String key = configMetas.genKey();
        Long oldVersion = VERSIONS.getOrDefault(key, -1L);
        if (newVersion > oldVersion) {
            System.out.println("===> [hefconfig] current version: " + newVersion + " old version: " + oldVersion);
            Map<String, String> newConfigs = findAllConfigs();
            VERSIONS.put(key, newVersion);
            CONFIGS.put(key, newConfigs);
            System.out.println("===> [hefconfig] fire an EnvironmentChangeEvent with keys: " + newConfigs.keySet());
            listenerList.forEach(listener->listener.onChange(new ChangeEvent(configMetas, newConfigs)));
        }
    }


}
