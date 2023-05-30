package com.chat.core.log.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @classDesc: 功能描述:(controller日志打印注解)
 * @author: cy
 * @date: 2022/11/21 15:30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface LogForAdaptor {
    /**是否打印参数*/
    boolean argsNeed() default true;
    /**是否打印结果*/
    boolean resultNeed() default true;
    /**是否打印耗时*/
    boolean millisecondNeed() default true;
}
