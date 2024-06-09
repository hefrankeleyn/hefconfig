package io.github.hefrankeleyn.hefconfigserver.service;

import io.github.hefrankeleyn.hefconfigserver.beans.Configs;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Map;

public interface HefConfigService {

    /**
     * 查询配置列表
     * @param capp
     * @param cenv
     * @param cnamespace
     * @return
     */
    List<Configs> findConfigsList(String capp, String cenv, String cnamespace);

    List<Configs> updateOrInsert(String capp, String cenv, String cnamespace, Map<String, String> ckeyvalueMap);

    Long version(String capp, String cenv, String cnamespace);
    Long updateOrInsertVersion(String capp, String cenv, String cnamespace, Long version);

    DeferredResult<Long> deferredResultVersion(String capp, String cenv, String cnamespace);

}
