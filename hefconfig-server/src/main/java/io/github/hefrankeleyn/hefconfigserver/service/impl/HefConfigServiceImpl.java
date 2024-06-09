package io.github.hefrankeleyn.hefconfigserver.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.github.hefrankeleyn.hefconfigserver.beans.Configs;
import io.github.hefrankeleyn.hefconfigserver.dao.HefConfigMapper;
import io.github.hefrankeleyn.hefconfigserver.service.HefConfigService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.async.DeferredResult;

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

    private static final Logger log = LoggerFactory.getLogger(HefConfigServiceImpl.class);
    @Resource
    private HefConfigMapper hefConfigMapper;
    private final MultiValueMap<String, DeferredResult<Long>> deferredResultVersions = new LinkedMultiValueMap<>();

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
        Long version = updateOrInsertVersion(capp, cenv, cnamespace, System.currentTimeMillis());
        List<Configs> list = ckeyvalueMap.keySet().stream().map(ckey -> new Configs(capp, cenv, cnamespace, ckey, ckeyvalueMap.get(ckey))).toList();
        for (Configs configs : list) {
            Configs oldConfigs = hefConfigMapper.findConfigs(capp, cenv, cnamespace, configs.getCkey());
            if (Objects.isNull(oldConfigs) || Objects.isNull(oldConfigs.getCapp())) {
                hefConfigMapper.insertConfigs(configs);
            } else {
                hefConfigMapper.updateConfigs(configs);
            }
        }
        addDeferredResult(capp, cenv, cnamespace, version);
        return findConfigsList(capp, cenv, cnamespace);
    }


    private void addDeferredResult(String capp, String cenv, String cnamespace, Long version) {
        String key = genKey(capp, cenv, cnamespace);
        List<DeferredResult<Long>> deferredResults = deferredResultVersions.get(key);
        if (Objects.nonNull(deferredResults) && !deferredResults.isEmpty()) {
            for (DeferredResult<Long> deferredResult : deferredResults) {
                deferredResult.setResult(version);
            }
        }
    }

    private static String genKey(String capp, String cenv, String cnamespace) {
        String key  = Strings.lenientFormat("%s_%s_%s", capp, cenv, cnamespace);
        return key;
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

    @Override
    public DeferredResult<Long> deferredResultVersion(String capp, String cenv, String cnamespace) {
        DeferredResult<Long> result = new DeferredResult<>(3000L);
        String key = genKey(capp, cenv, cnamespace);
        result.onCompletion(()->{
            log.debug("deferred result version completed, remote key: {}", key);
            deferredResultVersions.remove(key);
        });
        result.onTimeout(()->{
            log.debug("===> deferred result version timed out, remote key: {}", key);
            deferredResultVersions.remove(key);
        });
        result.onError((e)->{
            log.error("==> deferred result version error, remote key: {}, err: {}", key, e.getMessage());
            deferredResultVersions.remove(key);
        });
        deferredResultVersions.add(key, result);
        return result;
    }
}

