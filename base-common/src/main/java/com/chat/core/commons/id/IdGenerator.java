package com.chat.core.commons.id;

import com.chat.core.commons.utils.SpringHelpers;
import com.chat.core.commons.id.impl.SnowflakeUpperGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Random;

/**
 * @classDesc: 功能描述:(主键id生成器)
 * @author: cy
 * @date: 2022/11/21 15:30
 */
@Slf4j
public class IdGenerator {

    public static final class IdGeneratorHolder {
        public static final IdGenerator GENERATOR = new IdGenerator();
    }

    public static IdGenerator ins() {
        return IdGeneratorHolder.GENERATOR;
    }

    private IdGeneratorInterface defaultGenerator;

    public IdGenerator() {
        Random random = new Random();
        long randomServiceId = random.nextInt(256);
        ApplicationContext context = SpringHelpers.context();
        SnowflakeUpperGenerator defaultGenerator = new SnowflakeUpperGenerator(randomServiceId);
        if (context != null) {
            Environment environment = context.getEnvironment();
            String applicationName = environment.getProperty("spring.application.name");
            if (!StringUtils.isEmpty(applicationName)) {
                String serviceId = environment.getProperty("inspur.service.id." + applicationName);
                if (!StringUtils.isEmpty(serviceId)) {
                    defaultGenerator = new SnowflakeUpperGenerator(Long.valueOf(serviceId));
                }
            }
        }
        this.defaultGenerator = defaultGenerator;
    }

    /**
     * 生成唯一ID
     *
     * @return
     */
    public Long generator() {
        Long generator = defaultGenerator.generator();
        return generator;
    }


    public static void main(String[] args) {
        Long id = IdGenerator.ins().generator();
        System.out.println(id);
    }
}
