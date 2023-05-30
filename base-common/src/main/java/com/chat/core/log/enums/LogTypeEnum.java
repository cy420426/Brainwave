package com.chat.core.log.enums;

import lombok.Getter;

/**
 * @classDesc: 功能描述:(日志类型)
 * @author: cy
 * @date: 2022/11/21 15:30
 */
@Getter
public enum LogTypeEnum {

    /**
     * http请求
     */
    HTTP("http"),

    /**
     * 数据库请求
     */
    DB("db"),

    /**
     * 远程调用
     */
    API("api"),

    /**
     * mq发送请求
     */
    MQ("mq"),

    /**
     * 手动日志
     */
    INNER("inner");

    /**
     * 日志类型的值
     */
    private String value;

    LogTypeEnum(String value) {
        this.value = value;
    }
}
