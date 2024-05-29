package io.github.hefrankeleyn.hefconfig.demo.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * @Date 2024/5/29
 * @Author lifei
 */
public class Cat implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, EnvironmentAware, InitializingBean, DisposableBean {

    private String color;

    public Cat() {
        System.out.println("====> Step 3 Cat Constructor...");
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("====> Step 6 Cat BeanNameAware......: " + name);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("===> Step 7 Cat BeanFactoryAware......: " + beanFactory.getClass().getName());
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("====> Step 8 Cat ApplicationContextAware......: " + applicationContext.getClass().getName());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("================> Step 10 Cat InitializingBean...");
    }

    public void init() {
        System.out.println("===> Step 11 Cat init...");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("====> Step 13 Cat DisposableBean destroy...");
    }

    public void stop() {
        System.out.println("====> Step 14 Cat stop...");
    }

    @Override
    public void setEnvironment(Environment environment) {
        System.out.println("=====> Step 8 Cat Environment...");
    }
}
