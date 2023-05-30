package com.chat.core.commons.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @classDesc: 功能描述:()
 * @author: cy
 * @date: 2022/11/21 15:30
 */
@Component
@Slf4j
public class Test1CacheRunable implements MemoryCacheRunnable {
    @Override
    public boolean standalone() {
        return true;
    }

    @Override
    public boolean open() {
        return true;
    }

    @Override
    public int interval() {
        return 1;
    }

    @Override
    public void run() {

        log.info("Test1CacheRunable.run,");

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
