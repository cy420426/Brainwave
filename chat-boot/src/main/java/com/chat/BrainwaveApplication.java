package com.chat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @classDesc:
 * @author: cy
 * @date: 2023/1/9 9:16
 */
@SpringBootApplication
@EnableScheduling
@MapperScan(basePackages = {"com.chat.infrastructure.mapper"})
public class BrainwaveApplication {
    public static void main(String[] args) {
        SpringApplication.run(BrainwaveApplication.class, args);
    }
}
