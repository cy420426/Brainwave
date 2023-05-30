package com.chat.infrastructure.util.redis.key;

/**
 * @classDesc: 功能描述:(key顶层接口)
 * @author: 曹越
 * @date: 2022/11/21 15:30
 */
public interface CacheKey {


    /**
     * 获取key
     *
     * @return
     */
    String get();

}
