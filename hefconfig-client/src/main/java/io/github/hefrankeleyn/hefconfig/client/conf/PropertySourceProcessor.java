package io.github.hefrankeleyn.hefconfig.client.conf;

import io.github.hefrankeleyn.hefconfig.client.beans.ConfigMetas;
import io.github.hefrankeleyn.hefconfig.client.service.HefConfigService;
import io.github.hefrankeleyn.hefconfig.client.service.impl.HefConfigServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;
import java.util.Map;

/**
 * @Date 2024/5/28
 * @Author lifei
 */
public class PropertySourceProcessor implements BeanFactoryPostProcessor, EnvironmentAware, PriorityOrdered {

    private Environment environment;

    private static final String HEF_PROPERTY_SOURCES = "hefPropertySources";
    private static final String HEF_PROPERTY_SOURCE = "hefPropertySource";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ConfigurableEnvironment configurableEnvironment = (ConfigurableEnvironment) environment;
        MutablePropertySources propertySources = configurableEnvironment.getPropertySources();
        if (propertySources.contains(HEF_PROPERTY_SOURCES)) {
            return;
        }
        // todo, 通过http请求，从hefconfig-server中
        String capp = environment.getProperty("hefconfig.app", "app01");
        String cenv = environment.getProperty("hefconfig.env", "dev");
        String cnamespace = environment.getProperty("hefconfig.namespace", "application");
        String cconfigServer = environment.getProperty("hefconfig.configServer", "http://localhost:9129");
        ConfigMetas configMetas = new ConfigMetas(capp, cenv, cnamespace, cconfigServer);
        HefConfigService defaultHefConfigService = HefConfigService.getDefault(configMetas);
        HefPropertySource hefPropertySource = new HefPropertySource(HEF_PROPERTY_SOURCE, defaultHefConfigService);
        // 假如有多套环境变量，就可以把它们统一放到CompositePropertySource里面
        CompositePropertySource compositePropertySource = new CompositePropertySource(HEF_PROPERTY_SOURCES);
        compositePropertySource.addPropertySource(hefPropertySource);
        propertySources.addFirst(compositePropertySource);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
