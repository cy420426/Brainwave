package com.chat.infrastructure.common;

import com.chat.infrastructure.common.constant.CacheKey;
import com.chat.infrastructure.util.redis.operator.StringCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisCounter {
    private final StringCache stringCache;

    public Integer incrementAndGet(String userId) {
        Long atomic = stringCache.atomic(CacheKey.getChat().key(userId).build(), 1L);
        return atomic.intValue();
    }

    public boolean reset() {
        Set keys = stringCache.keys(CacheKey.getChat().build());
        return stringCache.delBatch(keys);
    }
}