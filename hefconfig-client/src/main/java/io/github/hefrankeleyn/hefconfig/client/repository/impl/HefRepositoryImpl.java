package io.github.hefrankeleyn.hefconfig.client.repository.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import io.github.hefrankeleyn.hefconfig.client.beans.ConfigMetas;
import io.github.hefrankeleyn.hefconfig.client.beans.Configs;
import io.github.hefrankeleyn.hefconfig.client.conf.ChangeEvent;
import io.github.hefrankeleyn.hefconfig.client.conf.ChangeListener;
import io.github.hefrankeleyn.hefconfig.client.repository.HefRepository;
import io.github.hefrankeleyn.utils.http.HttpInvoker;

import java.util.*;

/**
 * @Date 2024/5/31
 * @Author lifei
 */
public class HefRepositoryImpl implements HefRepository {


    private final ConfigMetas configMetas;

//    private final ScheduledExecutorService executor;
    private Map<String, Long> VERSIONS = Maps.newHashMap();
    private Map<String, Map<String, String>> CONFIGS = Maps.newHashMap();

    private List<ChangeListener> listenerList = Lists.newArrayList();

    public void addListener(ChangeListener listener) {
        listenerList.add(listener);
    }


    public HefRepositoryImpl(ConfigMetas configMetas) {
        this.configMetas = configMetas;
//        executor = Executors.newSingleThreadScheduledExecutor();
//        executor.scheduleWithFixedDelay(this::hearbeat, 1000, 5000, TimeUnit.MILLISECONDS);
        Thread deferredResultThread = new Thread(this::deferredResultHeartbeat);
        deferredResultThread.setDaemon(true);
        deferredResultThread.start();
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

    /*
    使用DeferredResult 改造配置中心，遇到一些麻烦，不知道怎么搞，有人弄出来的指导一下：
    1. 如果不另启一个线程，只用while循环，config-demo 启动的时候会卡住，启动不成功；
    2. HttpInvoker 是有超时时间的，无法长时间卡住返回值为 DeferredResult的请求；
    3. HttpInvoker请求返回值是DeferredResult<Long> 的url， 返回的json是long还是DeferredResult<Long> ；


    使用DeferredResult 改造配置中心：
    1. 使用一个守护线程来监听配置版本的变化，线程里面执行while逻辑；
    2. 设置 HttpInvoker的超时时间要大于 DeferredResult 的超时时间；
    3. HttpInvoker请求返回值是DeferredResult<Long> 的API， 返回值是Long；

     */
    private void deferredResultHeartbeat() {
        String url = configMetas.deferredResultVersionPath();
        String key = configMetas.genKey();
        while (true) {
            try {
                System.out.println("===> begin deferred result heartbeat: " + url);
//                HttpInvoker httpInvoker = new OkHttpInvoker(1000000L);
//                String res = httpInvoker.get(url);
//                Long newVersion = new Gson().fromJson(res, Long.class);
                Long newVersion = HttpInvoker.httpGet(url, Long.class);
                System.out.println("===> end deferred result heartbeat: " + url);
                Long oldVersion = VERSIONS.getOrDefault(key, -1L);
                if (newVersion > oldVersion) {
                    System.out.println("===> [hefconfig] current version: " + newVersion + " old version: " + oldVersion);
                    Map<String, String> newConfigs = findAllConfigs();
                    VERSIONS.put(key, newVersion);
                    CONFIGS.put(key, newConfigs);
                    System.out.println("===> [hefconfig] fire an EnvironmentChangeEvent with keys: " + newConfigs.keySet());
                    listenerList.forEach(listener->listener.onChange(new ChangeEvent(configMetas, newConfigs)));
                }
            }catch (Exception e) {
                System.err.println("===> deferredResultHeartbeat: " + e.getMessage());
            }
        }
    }


}
