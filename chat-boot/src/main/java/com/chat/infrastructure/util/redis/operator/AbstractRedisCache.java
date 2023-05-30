package com.chat.infrastructure.util.redis.operator;

import com.chat.infrastructure.util.SpringHelpers;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @classDesc: 功能描述:()
 * @author: 曹越
 * @date: 2022/11/21 15:30
 */
public class AbstractRedisCache {

    protected RedisTemplate getRedisTemplate() {
        return SpringHelpers.context().getBean("redisClient", RedisTemplate.class);
    }
}
