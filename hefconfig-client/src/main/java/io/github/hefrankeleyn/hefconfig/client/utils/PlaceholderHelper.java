package io.github.hefrankeleyn.hefconfig.client.utils;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Set;
import java.util.Stack;

/**
 * @Date 2024/6/3
 * @Author lifei
 */
public class PlaceholderHelper {

    private static final String PLACEHOLDER_PREFIX = "${";
    private static final String PLACEHOLDER_SUFFIX = "}";
    private static final String SIMPLE_PREFIX = "{";
    private static final String EXPRESSION_PREFIX = "#{";
    private static final String VALUE_SEPARATOR = ":";

    private PlaceholderHelper(){}

    private static class PlaceholderHelperHolder {
        public static final PlaceholderHelper INSTANCE = new PlaceholderHelper();
    }

    public static PlaceholderHelper instance() {
        return PlaceholderHelperHolder.INSTANCE;
    }

    public Object resolvePropertyValue(ConfigurableBeanFactory configurableBeanFactory, String beanName, String placeholder) {
        // 初始解析，解析出字面量
        String strVal = configurableBeanFactory.resolveEmbeddedValue(placeholder);
        BeanDefinition beanDefinition = null;
        if (configurableBeanFactory.containsBean(beanName)) {
            beanDefinition = configurableBeanFactory.getMergedBeanDefinition(beanName);
        }
        // 处理expression
        return resolveBeanDefinitionString(configurableBeanFactory, strVal, beanDefinition);
    }

    private Object resolveBeanDefinitionString(ConfigurableBeanFactory configurableBeanFactory, String strVal, BeanDefinition beanDefinition) {
        if (Objects.isNull(configurableBeanFactory.getBeanExpressionResolver())) {
            return strVal;
        }
        Scope registeredScope = null;
        if (Objects.nonNull(beanDefinition) && Objects.nonNull(beanDefinition.getScope())) {
            registeredScope = configurableBeanFactory.getRegisteredScope(beanDefinition.getScope());
        }
        return configurableBeanFactory.getBeanExpressionResolver().evaluate(strVal, new BeanExpressionContext(configurableBeanFactory, registeredScope));
    }


    private boolean isNormalPlaceholder(String placeholder) {
        return placeholder.startsWith(PLACEHOLDER_PREFIX) && placeholder.endsWith(PLACEHOLDER_SUFFIX);
    }

    private boolean containsPlaceholder(String placeholder) {
        return placeholder.startsWith(EXPRESSION_PREFIX) && placeholder.endsWith(PLACEHOLDER_SUFFIX) &&
                placeholder.contains(PLACEHOLDER_PREFIX);
    }

    private int findPlaceholderEndIndex(String text, int placeholderBeginIndex) {
        int placeholderEndIndex = placeholderBeginIndex + PLACEHOLDER_PREFIX.length();
        int withinNestedPlaceholder = 0;
        while (placeholderEndIndex < text.length()) {
            if (StringUtils.substringMatch(text, placeholderEndIndex, SIMPLE_PREFIX)) {
                withinNestedPlaceholder++;
                placeholderEndIndex = placeholderEndIndex + SIMPLE_PREFIX.length();
            } else if (StringUtils.substringMatch(text, placeholderEndIndex, PLACEHOLDER_SUFFIX)) {
                if (withinNestedPlaceholder >0) {
                    withinNestedPlaceholder--;
                    placeholderEndIndex = placeholderEndIndex + PLACEHOLDER_SUFFIX.length();
                } else {
                    return placeholderEndIndex;
                }
            } else {
                placeholderEndIndex ++;
            }
        }
        return -1;
    }

    private String normalizeToPlaceholder(String text) {
        int startIndex = text.indexOf(PLACEHOLDER_PREFIX);
        if (startIndex == -1) {
            return null;
        }
        int endIndex = text.lastIndexOf(PLACEHOLDER_SUFFIX);
        if (endIndex == -1) {
            return null;
        }
        return text.substring(startIndex, endIndex + PLACEHOLDER_SUFFIX.length());
    }

    // #{new java.text.SimpleDateFormat(${xx.yy:${zz.kk:23}}....${xx.yy:${zz.kk:12}})}
    public Set<String> extractPlaceholderKeys(String spelText) {
        Set<String> result = Sets.newHashSet();
        if (Objects.isNull(spelText)) {
            return result;
        }
        if (!isNormalPlaceholder(spelText) && !containsPlaceholder(spelText)) {
            return result;
        }
        Stack<String> stack = new Stack<>();
        stack.push(spelText);
        while (!stack.isEmpty()) {
            String text = stack.pop();
            int placeholderBeginIndex = text.indexOf(PLACEHOLDER_PREFIX);
            if (placeholderBeginIndex == -1) {
                result.add(text);
                continue;
            }
            int placeholderEndIndex = findPlaceholderEndIndex(text, placeholderBeginIndex);
            if (placeholderEndIndex == -1) {
                continue;
            }
            String placeholderCandidate  = text.substring(placeholderBeginIndex + PLACEHOLDER_PREFIX.length(), placeholderEndIndex);
            // ${${}}
            if (placeholderCandidate.startsWith(PLACEHOLDER_PREFIX)) {
                stack.push(placeholderCandidate);
            } else {
                // key:{key2:value2}
                int separatorIndex = placeholderCandidate.indexOf(VALUE_SEPARATOR);
                if (separatorIndex == -1) {
                    stack.push(placeholderCandidate);
                }  else {
                    stack.push(placeholderCandidate.substring(0, separatorIndex));
                    String valuePlaceholder = normalizeToPlaceholder(placeholderCandidate.substring(separatorIndex + VALUE_SEPARATOR.length()));
                    if (StringUtils.hasText(valuePlaceholder)) {
                        stack.push(valuePlaceholder);
                    }

                }
            }

            if (placeholderEndIndex + PLACEHOLDER_SUFFIX.length() < text.length() -1) {
                String remainingPart = normalizeToPlaceholder(text.substring(placeholderEndIndex + PLACEHOLDER_SUFFIX.length()));
                if (StringUtils.hasText(remainingPart)) {
                    stack.push(remainingPart);
                }
            }
        }

        return result;
    }


}
