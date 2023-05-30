package com.chat.core.log.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @classDesc: 功能描述:(方法级日志打印注解)
 * @author: cy
 * @date: 2022/11/21 15:30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface LogForMethod {
    @AliasFor("description")
    String value() default "";
    /**超过定义的耗时会打印日志,作为慢日志输出*/
    long maxSlowTime() default 0L;
    /**描述*/
    @AliasFor("value")
    String description() default "";
    /**是否打印结果*/
    boolean resultNeed() default true;
}
