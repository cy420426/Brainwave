package com.chat.core.log.aop;

import com.chat.core.log.annotations.LogForAdaptor;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @classDesc: 功能描述:()
 * @author: cy
 * @date: 2022/11/21 15:30
 */
public class LogForAdaptorPointcut extends StaticMethodMatcherPointcutAdvisor {
    @Override
    public boolean matches(Method method, Class<?> aClass) {
        LogForAdaptor annotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), LogForAdaptor.class);
        return Objects.nonNull(annotation);
    }
}
