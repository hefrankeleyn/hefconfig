package io.github.hefrankeleyn.hefconfig.demo.conf;

import io.github.hefrankeleyn.hefconfig.demo.bean.Cat;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Date 2024/5/28
 * @Author lifei
 */
//@Configuration
public class CycleConfig {

//    @Bean(initMethod = "init", destroyMethod = "stop")
    public Cat cat() {
        return new Cat();
    }

//    @Bean
    public BeanFactoryPostProcessor cycleBeanFactoryPostProcessor() {
        BeanFactoryPostProcessor beanFactoryPostProcessor = new BeanFactoryPostProcessor() {
            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
                System.out.println("====> Step 1. 工厂后处理期开始运行： cycleBeanFactoryPostProcessor... ");
            }
        };
        return beanFactoryPostProcessor;
    }

//    @Bean
    public InstantiationAwareBeanPostProcessor cycleInstantiationAwareBeanPostProcessor() {
        return new InstantiationAwareBeanPostProcessor() {
            @Override
            public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
                if (beanClass == Cat.class) {
                    System.out.println("====> Step 2. InstantiationAwareBeanPostProcessor  运行， postProcessBeforeInstantiation， "+ beanClass.getName());
                }
                return InstantiationAwareBeanPostProcessor.super.postProcessBeforeInstantiation(beanClass, beanName);
            }

            @Override
            public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
                if (bean.getClass() == Cat.class) {
                    System.out.println("=====> Step 4. InstantiationAwareBeanPostProcessor postProcessAfterInstantiation. " +bean.getClass().getName());
                }
                return InstantiationAwareBeanPostProcessor.super.postProcessAfterInstantiation(bean, beanName);
            }

            @Override
            public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
                if (bean.getClass() == Cat.class) {
                    System.out.println("====> Step 5. InstantiationAwareBeanPostProcessor postProcessProperties. " +bean.getClass().getName());
                }
                return InstantiationAwareBeanPostProcessor.super.postProcessProperties(pvs, bean, beanName);
            }
        };
    }

//    @Bean
    public BeanPostProcessor cycleBeanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (bean.getClass() == Cat.class) {
                    System.out.println("==================> Step 9. BeanPostProcessor postProcessBeforeInitialization..." + bean.getClass().getName());
                }

                return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
            }

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean.getClass() == Cat.class) {
                    System.out.println("===============> Step 12. BeanPostProcessor postProcessAfterInitialization..." + bean.getClass().getName());
                }
                return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
            }
        };
    }


}
