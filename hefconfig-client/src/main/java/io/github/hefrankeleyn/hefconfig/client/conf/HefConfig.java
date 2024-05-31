package io.github.hefrankeleyn.hefconfig.client.conf;

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
        System.out.println("====> register PropertySourceProcessor....");
        Optional<String> any = Arrays.stream(registry.getBeanDefinitionNames()).filter(item -> PropertySourceProcessor.class.getName().equals(item)).findAny();
        if (any.isPresent()) {
            System.out.println("============> already registry PropertySourceProcessor");
        } else {
            AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(PropertySourceProcessor.class).getBeanDefinition();
            registry.registerBeanDefinition(PropertySourceProcessor.class.getName(), beanDefinition);
        }
    }
}
