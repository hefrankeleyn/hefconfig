package io.github.hefrankeleyn.hefconfig.client.beans;

import com.google.common.base.MoreObjects;

import java.lang.reflect.Field;

/**
 * @Date 2024/6/4
 * @Author lifei
 */
public class SpringValue {
    private Object bean;
    private String beanName;
    private String key;
    private String placeholder;
    private Field field;

    public SpringValue() {
    }

    public SpringValue(Object bean, String beanName, String key, String placeholder, Field field) {
        this.bean = bean;
        this.beanName = beanName;
        this.key = key;
        this.placeholder = placeholder;
        this.field = field;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(SpringValue.class)
                .add("bean", bean)
                .add("beanName", beanName)
                .add("key", key)
                .add("placeholder", placeholder)
                .add("field", field)
                .toString();
    }
}
