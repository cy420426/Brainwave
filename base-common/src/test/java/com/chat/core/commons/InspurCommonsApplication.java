package com.chat.core.commons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @classDesc: 功能描述:()
 * @author: cy
 * @date: 2022/11/21 15:30
 */
@SpringBootApplication(scanBasePackages = "com.inspur.core.commons")
public class InspurCommonsApplication {

    public static void main(String[] args) {

        SpringApplication.run(InspurCommonsApplication.class, args);

        synchronized (InspurCommonsApplication.class) {
            while (true) {
                try {
                    InspurCommonsApplication.class.wait();
                } catch (Exception e) {

                }
            }
        }

    }
}
