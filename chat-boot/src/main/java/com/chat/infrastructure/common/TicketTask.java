package com.chat.infrastructure.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @classDesc:
 * @author: cyjer
 * @date: 2023/5/16 16:45
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TicketTask {
    private final RedisCounter redisCounter;

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetCounter() {
        boolean reset = redisCounter.reset();
        log.info("重置计数结果:{}", reset);
    }

}
