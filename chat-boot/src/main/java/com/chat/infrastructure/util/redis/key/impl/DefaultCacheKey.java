package com.chat.infrastructure.util.redis.key.impl;

import com.chat.infrastructure.util.redis.key.CacheKey;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @classDesc: 功能描述:(默认key构造器)
 * @author: 曹越
 * @date: 2022/11/21 15:30
 */
@Builder
@Data
@Slf4j
public class DefaultCacheKey implements CacheKey {

    private static final String SPLIT = "::";

    private String group;

    private String key;

    @Override
    public String get() {
        return StringUtils.isNotBlank(key) ? group + SPLIT + key : group + SPLIT;
    }
}
