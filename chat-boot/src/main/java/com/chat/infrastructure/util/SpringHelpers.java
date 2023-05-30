package com.chat.infrastructure.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @classDesc:
 * @author: cyjer
 * @date: 2023/1/30 9:53
 */
@Slf4j
@Configuration
@AutoConfigureOrder(value = Ordered.HIGHEST_PRECEDENCE)
public class SpringHelpers implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext context() {
        return context;
    }

    public static void setContext(ApplicationContext c) {
        context = c;
    }

}
