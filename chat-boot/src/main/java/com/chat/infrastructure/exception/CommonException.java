package com.chat.infrastructure.exception;

import com.chat.core.commons.code.BasicResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @classDesc:
 * @author: cyjer
 * @date: 2023/2/11 17:42
 */
@AllArgsConstructor
@Getter
public enum CommonException implements BasicResponseCode {
    /**
     * 系统报错
     */
    SYSTEM_ERROR("8888", "系统出错了~~"),
    CHAT_ERROR("8889", "小主~您的对话次数已不足~"),
    ROOM_ERROR("8890", "小主~您只能创建这些房间了~"),
    CHAR_ERROR("8891", "小主~您输入的内容太长了~"),
    ;

    private String code;
    private String msg;

    public String prefix() {
        return "chat";
    }

}
