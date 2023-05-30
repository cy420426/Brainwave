package com.chat.domain.assistant.cache;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.lang.Validator;
import com.chat.domain.assistant.DTO.ChatMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 携带信息
 */
public class ChatCache {

    public static final Cache<String, List<ChatMessage>> chats = CacheUtil.newLRUCache(10000);

    public static void put(String key, List<ChatMessage> value){
        int listSize = value.size();
        List<ChatMessage> newMessages = value;
        if (listSize > 10){
            newMessages = value.subList(listSize-10,listSize);
        }
        chats.put(key,newMessages);
    }

    public static List<ChatMessage> get(String key){
        List<ChatMessage> messages = chats.get(key);
        if (Validator.isEmpty(messages)){
            return new ArrayList<>();
        }else {
            return messages;
        }
    }
}
