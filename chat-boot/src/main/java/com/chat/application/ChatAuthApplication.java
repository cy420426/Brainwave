package com.chat.application;

/**
 * chat 相关鉴权接口
 */
public interface ChatAuthApplication {
    // 获取当前操作用户ID
    default Long getUserId(){
        return 0L;
    }

    default String getUserName(){
        return "匿名用户";
    }
}
