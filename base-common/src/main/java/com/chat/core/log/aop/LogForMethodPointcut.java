package com.chat.core.log.aop;

import com.chat.core.log.annotations.LogForMethod;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;

/**
 * @classDesc: 功能描述:()
 * @author: cy
 * @date: 2022/11/21 15:30
 */
public class LogForMethodPointcut extends StaticMethodMatcherPointcutAdvisor {
    @Override
    public boolean matches(Method method, Class<?> aClass) {
        return AnnotatedElementUtils.hasAnnotation(method, LogForMethod.class);
    }
}
