package com.chat.core.commons.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @classDesc: 功能描述:()
 * @author: cy
 * @date: 2022/11/21 15:30
 */
@Component
@Slf4j
public class Test2CacheRunable implements MemoryCacheRunnable {

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

        log.info("Test2CacheRunable.run,");

    }
}
