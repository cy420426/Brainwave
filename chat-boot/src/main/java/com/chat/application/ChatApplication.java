package com.chat.application;

import com.chat.domain.assistant.DTO.ChatMessage;
import com.chat.domain.assistant.BO.ChatParams;
import com.chat.domain.assistant.transfer.ChatResult;

import java.util.List;
import java.util.Map;

/**
 * ChatGpt
 */
public interface ChatApplication {
    // 构建请求头
    Map<String, String> headers();

    // 构建用户消息
    ChatMessage buildUserMessage(String content);

    // 构建会话
    ChatResult doChat(ChatParams params);

    // 构建会话 -- 携带会话ID
    String doChat(ChatParams params, String chatId, String content);

    // 获取上下文
    List<ChatMessage> getContext(String chatId);

    // 获取上下文 -- 指定条数
    List<ChatMessage> getContext(String chatId, Integer num);

    // 简化返回结果
    String simpleResult(ChatResult result);

}
