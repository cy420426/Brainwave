package com.chat.core.log.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.util.ResourceUtils;

import java.util.Properties;

/**
 * @classDesc: 功能描述:(统一配置logback)
 * @author: cy
 * @date: 2022/11/21 15:30
 */
@Slf4j
public class LoggingEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String LOG_DIY = "use.syslog";
    private static final String TRUE = "true";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String logConfig = environment.getProperty(LOG_DIY);
        //使用自定义本身的log-config
        if (TRUE.equals(logConfig)) {
            return;
        }
        Properties props = new Properties();
        String newValue = "classpath:logback-chat.xml";
        try {
            ResourceUtils.getURL(newValue);
        } catch (Exception ex) {
            log.error("can not find logback config url..", ex);
            return;
        }
        props.put(LoggingApplicationListener.CONFIG_PROPERTY, newValue);
        environment.getPropertySources().addFirst(new PropertiesPropertySource(LoggingApplicationListener.CONFIG_PROPERTY + "_properties", props));
    }

}

