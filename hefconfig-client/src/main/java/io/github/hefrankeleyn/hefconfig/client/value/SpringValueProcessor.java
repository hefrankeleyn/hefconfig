package io.github.hefrankeleyn.hefconfig.client.value;

import com.google.common.base.Throwables;
import io.github.hefrankeleyn.hefconfig.client.beans.SpringValue;
import io.github.hefrankeleyn.hefconfig.client.utils.PlaceholderHelper;
import io.github.hefrankeleyn.utils.FieldUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.*;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @Date 2024/6/3
 * @Author lifei
 */
public class SpringValueProcessor implements BeanFactoryAware, BeanPostProcessor, ApplicationListener<EnvironmentChangeEvent> {

    private BeanFactory beanFactory;
    private final MultiValueMap<String, SpringValue> VALUE_HOLDERS = new LinkedMultiValueMap<>();
    private final PlaceholderHelper placeholderHelper = PlaceholderHelper.instance();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 扫描带有@Value的字段
        List<Field> valueFields = FieldUtils.findAnnotatedFields(bean.getClass(), Value.class);
        // 遍历 valueFields， 提取表达式中配置的key

        for (Field valueField : valueFields) {
            Value valueAnnotation = valueField.getAnnotation(Value.class);
            String spel = valueAnnotation.value();
            Set<String> keys = placeholderHelper.extractPlaceholderKeys(spel);
            for (String key : keys) {
                VALUE_HOLDERS.add(key, new SpringValue(bean, beanName, key, spel, valueField));
            }
        }
        return bean;
    }

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        event.getKeys().forEach(key->{
            List<SpringValue> springValues = VALUE_HOLDERS.get(key);
            if (Objects.isNull(springValues) || springValues.isEmpty()) {
                return;
            }
            try {
                for (SpringValue springValue : springValues) {
                    Object fieldValue = placeholderHelper.resolvePropertyValue((ConfigurableBeanFactory) beanFactory,
                            springValue.getBeanName(), springValue.getPlaceholder());
                    Field field = springValue.getField();
                    field.setAccessible(true);
                    field.set(springValue.getBean(), fieldValue);
                }
            }catch (Exception e) {
                System.err.println(Throwables.getStackTraceAsString(e));
            }
        });
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
