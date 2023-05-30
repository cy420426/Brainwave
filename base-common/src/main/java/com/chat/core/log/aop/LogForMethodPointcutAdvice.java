package com.chat.core.log.aop;

import com.alibaba.fastjson.JSON;
import com.chat.core.log.annotations.LogForMethod;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @classDesc: 功能描述:(方法级日志打印)
 * @author: cy
 * @date: 2022/11/21 15:30
 */
public class LogForMethodPointcutAdvice implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        long start = System.currentTimeMillis();
        Method method = methodInvocation.getMethod();
        Logger logger = LoggerFactory.getLogger(method.getDeclaringClass());
        String args = Arrays.toString(methodInvocation.getArguments());
        LogForMethod logForTimeConsumer = AnnotationUtils.findAnnotation(method, LogForMethod.class);
        long milliseconds = this.getMillisecond(logForTimeConsumer);
        String description = this.getDescription(logForTimeConsumer);
        boolean resultNeed = this.getResultNeed(logForTimeConsumer);
        logger.info(description + ",method:{},方法入参:{}", method.getName(), args);
        Object result = methodInvocation.proceed();
        long end = System.currentTimeMillis();
        if (end - start > milliseconds && milliseconds != 0L) {
            logger.warn(description + ",slow.method:{},方法耗时:{}ms", method.getName(), end - start);
            if (resultNeed) {
                logger.warn(description + ",slow.method:{},方法出参:{}", method.getName(), JSON.toJSONString(result));
            }
        } else {
            logger.info(description + ", method:{},方法耗时:{}ms", method.getName(), end - start);
            if (resultNeed) {
                logger.info(description + ", method:{},方法出参:{}", method.getName(), JSON.toJSONString(result));
            }
        }
        return result;
    }

    private String getDescription(LogForMethod logForTimeConsumer) {
        return logForTimeConsumer.description();
    }

    private long getMillisecond(LogForMethod logForTimeConsumer) {
        return logForTimeConsumer.maxSlowTime();
    }

    private boolean getResultNeed(LogForMethod logForTimeConsumer) {
        return logForTimeConsumer.resultNeed();
    }
}
