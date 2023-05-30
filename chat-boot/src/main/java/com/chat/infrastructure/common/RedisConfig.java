package com.chat.infrastructure.common;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @classDesc:
 * @author: cyjer
 * @date: 2023/1/31 15:14
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class RedisConfig {

    @Bean("redisClient")
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        FastJsonRedisSerializer<Object> serializer = new FastJsonRedisSerializer<>(Object.class);
        template.setKeySerializer(serializer);
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(serializer);
        template.setHashValueSerializer(serializer);
        return template;
    }

}
