package com.chat.core.commons.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @classDesc: 不包装ResponseResult
 * @author: cy
 * @date: 2023/1/11 9:55
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface NoResponseResult {
}
