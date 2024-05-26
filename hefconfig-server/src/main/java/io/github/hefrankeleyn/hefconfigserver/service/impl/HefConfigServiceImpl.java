package io.github.hefrankeleyn.hefconfigserver.service.impl;

import com.google.common.collect.Lists;
import io.github.hefrankeleyn.hefconfigserver.beans.Configs;
import io.github.hefrankeleyn.hefconfigserver.dao.HefConfigMapper;
import io.github.hefrankeleyn.hefconfigserver.service.HefConfigService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @Date 2024/5/26
 * @Author lifei
 */
@Service
public class HefConfigServiceImpl implements HefConfigService {

    @Resource
    private HefConfigMapper hefConfigMapper;

    @Override
    public List<Configs> findConfigsList(String capp, String cenv, String cnamespace) {
        List<Configs> result = Lists.newArrayList();
        List<Configs> configsList = hefConfigMapper.findConfigsList(capp, cenv, cnamespace);
        if (Objects.nonNull(configsList) && !configsList.isEmpty()) {
            result.addAll(configsList);
        }
        return result;
    }

    @Override
    public List<Configs> updateOrInsert(String capp, String cenv, String cnamespace, Map<String, String> ckeyvalueMap) {
        List<Configs> result = Lists.newArrayList();
        if (Objects.isNull(ckeyvalueMap) || ckeyvalueMap.isEmpty()) {
            return result;
        }
        updateOrInsertVersion(capp, cenv, cnamespace, System.currentTimeMillis());
        List<Configs> list = ckeyvalueMap.keySet().stream().map(ckey -> new Configs(capp, cenv, cnamespace, ckey, ckeyvalueMap.get(ckey))).toList();
        for (Configs configs : list) {
            Configs oldConfigs = hefConfigMapper.findConfigs(capp, cenv, cnamespace, configs.getCkey());
            if (Objects.isNull(oldConfigs) || Objects.isNull(oldConfigs.getCapp())) {
                hefConfigMapper.insertConfigs(configs);
            } else {
                hefConfigMapper.updateConfigs(configs);
            }
        }
        return findConfigsList(capp, cenv, cnamespace);
    }

    @Override
    public Long updateOrInsertVersion(String capp, String cenv, String cnamespace, Long version) {
        Long oldVersion = hefConfigMapper.findConfigsVersion(capp, cenv, cnamespace);
        if (Objects.isNull(oldVersion)) {
            hefConfigMapper.insertConfigsVersion(capp, cenv, cnamespace, version);
        } else {
            hefConfigMapper.updateConfigsVersion(capp, cenv, cnamespace, version);
        }
        return version;
    }

    @Override
    public Long version(String capp, String cenv, String cnamespace) {
        Long version = hefConfigMapper.findConfigsVersion(capp, cenv, cnamespace);
        return Optional.ofNullable(version).orElse(-1L);
    }

}

