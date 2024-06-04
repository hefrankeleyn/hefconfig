package io.github.hefrankeleyn.hefconfig.client.conf;

import io.github.hefrankeleyn.hefconfig.client.value.SpringValueProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.Optional;

/**
 * @Date 2024/5/28
 * @Author lifei
 */
public class HefConfig implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registerBean(registry, PropertySourceProcessor.class);
        registerBean(registry, SpringValueProcessor.class);
    }

    private void registerBean(BeanDefinitionRegistry registry, Class<?> beanClass) {
        System.out.println("====> ready register: " + beanClass.getName());
        Optional<String> any = Arrays.stream(registry.getBeanDefinitionNames()).filter(item -> beanClass.getName().equals(item)).findAny();
        if (any.isPresent()) {
            System.out.println("============> already registry " + beanClass.getName());
        } else {
            AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(beanClass).getBeanDefinition();
            registry.registerBeanDefinition(beanClass.getName(), beanDefinition);
        }
    }


}
