package com.chat.infrastructure.util.redis.operator.impl;

import com.chat.infrastructure.util.redis.key.CacheKey;
import com.chat.infrastructure.util.redis.operator.AbstractRedisCache;
import com.chat.infrastructure.util.redis.operator.ListCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @classDesc: 功能描述:(List操作)
 * @author: 曹越
 * @date: 2022/11/21 15:30
 */
@Slf4j
@Component
public class RedisListCacheImpl<V> extends AbstractRedisCache implements ListCache<V> {

    @Override
    public List<V> pop(CacheKey key, int size) {
        try {

            List<V> list = new ArrayList<>();

            while (true) {
                if (size == 0) {
                    break;
                }

                V pop = (V) getRedisTemplate().opsForList().leftPop(key.get());
                if (pop != null) {
                    list.add(pop);
                }
                size--;
            }

            return list;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean add(CacheKey key, V value) {
        try {
            getRedisTemplate().opsForList().leftPush(key.get(), value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public boolean remove(CacheKey key, V value) {
        try {
            getRedisTemplate().opsForList().remove(key.get(), 1, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public boolean save(CacheKey key, List<V> values, Long expire) {
        try {
            getRedisTemplate().opsForList().leftPushAll(key.get(), values);
            if (expire != -1) {
                expire(key, expire);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public List<V> get(CacheKey key) {
        ListOperations<String, V> ops = getRedisTemplate().opsForList();
        return ops.range(key.get(), 0, ops.size(key.get()));
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
    public boolean update(CacheKey key, List<V> value) {
        try {
            del(key);
            getRedisTemplate().opsForList().leftPushAll(key.get(), value);

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
            Long size = getRedisTemplate().opsForList().size(key.get());
            if (size == null || size == 0) {
                return false;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public Long size(CacheKey key) {
        return getRedisTemplate().opsForList().size(key.get());
    }
}
