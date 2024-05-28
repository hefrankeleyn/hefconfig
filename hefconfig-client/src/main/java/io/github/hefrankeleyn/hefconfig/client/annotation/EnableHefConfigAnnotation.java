package io.github.hefrankeleyn.hefconfig.client.annotation;

import io.github.hefrankeleyn.hefconfig.client.conf.HefConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * hef config client 入口
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Import(value = {HefConfig.class})
public @interface EnableHefConfigAnnotation {
}
