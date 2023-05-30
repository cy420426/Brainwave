package com.chat.core.log.aop;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 * @classDesc: 功能描述:()
 * @author: cy
 * @date: 2022/11/21 15:30
 */
public class LogForAdaptorPointcutAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    @Override
    public Pointcut getPointcut() {
        return new LogForAdaptorPointcut();
    }

    @Override
    public Advice getAdvice() {
        return new LogForAdaptorPointcutAdvice();
    }
}
