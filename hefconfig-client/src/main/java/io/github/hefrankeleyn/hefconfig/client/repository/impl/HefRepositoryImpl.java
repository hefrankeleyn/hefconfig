package io.github.hefrankeleyn.hefconfig.client.repository.impl;

import com.google.common.base.Strings;
import com.google.gson.reflect.TypeToken;
import io.github.hefrankeleyn.hefconfig.client.beans.ConfigMetas;
import io.github.hefrankeleyn.hefconfig.client.beans.Configs;
import io.github.hefrankeleyn.hefconfig.client.repository.HefRepository;
import io.github.hefrankeleyn.utils.http.HttpInvoker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Date 2024/5/31
 * @Author lifei
 */
public class HefRepositoryImpl implements HefRepository {

    private final ConfigMetas configMetas;

    public HefRepositoryImpl(ConfigMetas configMetas) {
        this.configMetas = configMetas;
    }

    @Override
    public Map<String, String> getConfig() {
        String url = Strings.lenientFormat("%s/hefConfigController/list?capp=%s&cenv=%s&cnamespace=%s",
                configMetas.getCconfigServer(), configMetas.getCapp(), configMetas.getCenv(), configMetas.getCnamespace());
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


}
