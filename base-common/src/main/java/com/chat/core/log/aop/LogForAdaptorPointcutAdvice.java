package com.chat.core.log.aop;

import com.alibaba.fastjson.JSON;
import com.chat.core.log.annotations.LogForAdaptor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @classDesc: 功能描述:(controller日志打印拦截器)
 * @author: cy
 * @date: 2022/11/21 15:30
 */
@Slf4j
public class LogForAdaptorPointcutAdvice implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        Class<?> clazz = method.getDeclaringClass();
        if (clazz.getDeclaredAnnotation(RestController.class) != null || clazz.getDeclaredAnnotation(Controller.class) != null) {
            LogForAdaptor logs = clazz.getDeclaredAnnotation(LogForAdaptor.class);
            boolean argsNeed = logs.argsNeed();
            boolean resultNeed = logs.resultNeed();
            boolean millisecondNeed = logs.millisecondNeed();
            //获取request
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            //http请求的方法
            ServletRequestAttributes ra = (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request;
            //请求路径
            request = ra.getRequest();
            String servletPath = request.getServletPath();
            if (argsNeed) {
                log.info("path:{},入参:{}", servletPath, Arrays.toString(methodInvocation.getArguments()));
            }
            long start = System.currentTimeMillis();
            Object result = methodInvocation.proceed();
            if (millisecondNeed) {
                long end = System.currentTimeMillis();
                log.info("path:{},处理总耗时:{}ms", servletPath, end - start);
            }
            if (resultNeed) {
                log.info("path:{},出参:{}", servletPath, JSON.toJSONString(result));
            }
            return result;
        }
        return null;
    }
}
