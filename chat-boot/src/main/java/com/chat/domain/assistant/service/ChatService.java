package com.chat.domain.assistant.service;

import com.chat.application.ChatApplication;
import com.chat.application.ChatAuthApplication;
import com.chat.core.commons.exception.BusinessException;
import com.chat.core.commons.id.IdGenerator;
import com.chat.domain.assistant.BO.ChatParams;
import com.chat.domain.assistant.BO.ChoiceModel;
import com.chat.domain.assistant.DTO.ChatMessage;
import com.chat.domain.assistant.cache.ChatCache;
import com.chat.domain.assistant.transfer.ChatApi;
import com.chat.domain.assistant.transfer.ChatResult;
import com.chat.domain.user.event.ReduceChatCountEvent;
import com.chat.domain.user.event.SaveChatLogEvent;
import com.chat.infrastructure.common.RedisCounter;
import com.chat.infrastructure.common.constant.CacheKey;
import com.chat.infrastructure.common.constant.ChatRoleConst;
import com.chat.infrastructure.exception.CommonException;
import com.chat.infrastructure.po.ChatRoomLog;
import com.chat.infrastructure.util.redis.operator.ListCache;
import com.dtflys.forest.http.ForestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OpenAI相关服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService implements ChatApplication {
    @Value("${openai.chat.key}")
    private String chatKey;
    private final ChatApi chatApi;
    private final ChatAuthApplication authService;
    private final ApplicationEventPublisher publisher;
    private final ListCache<Long> cache;
    private final RedisCounter redisCounter;

    @Override
    public Map<String, String> headers() {
        Map<String, String> headers = new HashMap<>();
        String apiToken = chatKey;
        headers.put("Authorization", "Bearer " + apiToken);
        return headers;
    }

    @Override
    public ChatMessage buildUserMessage(String content) {
        ChatMessage message = new ChatMessage();
        message.setRole(ChatRoleConst.USER);
        message.setContent(content);
        return message;
    }

    @Override
    public ChatResult doChat(ChatParams params) {
        // 写入用户ID
        params.setUser(String.valueOf(authService.getUserId()));
        ForestResponse<ChatResult> response = chatApi.ChatCompletions(headers(), params);
        ChatResult result = response.getResult();
        return result;
    }

    @Override
    public String doChat(ChatParams params, String chatId, String content) {
        // 写入用户ID
        params.setUser(String.valueOf(authService.getUserId()));
        Integer count = redisCounter.incrementAndGet(params.getUser());
        if (count > CacheKey.CHAT_TICKET_COUNT) {
            throw BusinessException.build(CommonException.CHAT_ERROR);
        }
        ForestResponse<ChatResult> response = chatApi.ChatCompletions(headers(), params);
        if (!response.isSuccess()) {
            return null;
        }
        ChatResult result = response.getResult();
        // 获取返回结果
        List<ChoiceModel> choices = result.getChoices();
        // 获取第一条结果
        ChoiceModel choice = choices.get(0);
        // 获取消息
        ChatMessage message = choice.getMessage();
        // 本地缓存用户聊天上下文，便于下次携带
        List<ChatMessage> messages = ChatCache.get(chatId);
        messages.add(message);
        ChatCache.put(chatId, messages);
        String res = this.simpleResult(result);
        //记录聊天日志信息
        ChatRoomLog chatRoomLog = new ChatRoomLog();
        chatRoomLog.setId(IdGenerator.ins().generator());
        chatRoomLog.setChatRoomId(chatId);
        chatRoomLog.setRequest(content);
        chatRoomLog.setUserId(params.getUser());
        chatRoomLog.setResponse(res);
        chatRoomLog.setReqRole(ChatRoleConst.USER);
        chatRoomLog.setResRole(message.getRole());
        //减少聊天次数
        publisher.publishEvent(new ReduceChatCountEvent(this, params.getUser()));
        //请求记录
        publisher.publishEvent(new SaveChatLogEvent(this, chatRoomLog));
        // 返回结果
        return res;
    }


    @Override
    public List<ChatMessage> getContext(String chatId) {
        List<ChatMessage> messages = ChatCache.get(chatId);
        return messages;
    }


    @Override
    public List<ChatMessage> getContext(String chatId, Integer num) {
        List<ChatMessage> messages = getContext(chatId);
        int msgSize = messages.size();

        if (msgSize > num) {
            return messages.subList(msgSize - num, msgSize);
        } else {
            return messages;
        }
    }

    @Override
    public String simpleResult(ChatResult result) {
        // 获取返回结果
        List<ChoiceModel> choices = result.getChoices();
        // 获取第一条结果
        ChoiceModel choice = choices.get(0);
        // 获取消息
        ChatMessage message = choice.getMessage();
        // 返回内容
        return message.getContent();
    }
}
