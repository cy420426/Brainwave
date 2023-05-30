package com.chat.infrastructure.util.redis.operator.impl;

import com.chat.infrastructure.util.redis.key.CacheKey;
import com.chat.infrastructure.util.redis.operator.AbstractRedisCache;
import com.chat.infrastructure.util.redis.operator.MapCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @classDesc: 功能描述:(map操作)
 * @author: 曹越
 * @date: 2022/11/21 15:30
 */
@Slf4j
@Component
public class RedisMapCacheImpl<K, V> extends AbstractRedisCache implements MapCache<K, V> {

    @Override
    public V getValue(CacheKey key, K mapKey) {

        try {
            V value = (V) getRedisTemplate().opsForHash().entries(key.get()).get(mapKey);
            return value;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean put(CacheKey key, K mapKey, V value) {
        try {
            getRedisTemplate().opsForHash().put(key.get(), mapKey, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public boolean remove(CacheKey key, K mapKey) {
        try {
            getRedisTemplate().opsForHash().delete(key.get(), mapKey);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public Long size(CacheKey key) {
        try {
            Long size = getRedisTemplate().opsForHash().size(key.get());
            return size;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return 0L;
        }
    }

    @Override
    public boolean putAll(CacheKey key, Map<K, V> map) {
        try {
            getRedisTemplate().opsForHash().putAll(key.get(), map);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }


    @Override
    public boolean save(CacheKey key, Map<K, V> value, Long expire) {
        try {
            getRedisTemplate().opsForHash().putAll(key.get(), value);
            expire(key, expire);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public Map<K, V> get(CacheKey key) {
        try {
            Map<K, V> map = getRedisTemplate().opsForHash().entries(key.get());
            return map;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean del(CacheKey key) {
        try {
            getRedisTemplate().delete(key.get());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public boolean update(CacheKey key, Map<K, V> value) {
        try {
            value.entrySet().forEach(item -> getRedisTemplate().opsForHash().put(key.get(), item.getKey(), item.getValue()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }


    @Override
    public boolean expire(CacheKey key, Long expire) {
        try {
            getRedisTemplate().expire(key.get(), expire, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public boolean exits(CacheKey key) {
        try {
            Long size = getRedisTemplate().opsForHash().size(key.get());
            if (size == null || size == 0) {
                return false;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }
}
