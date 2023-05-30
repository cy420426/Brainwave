package com.chat.domain.user.listener;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.chat.domain.user.event.CheckAndReduceChatRoomCreateCountEvent;
import com.chat.domain.user.event.InitChatEvent;
import com.chat.domain.user.event.ReduceChatCountEvent;
import com.chat.domain.user.event.SaveChatLogEvent;
import com.chat.domain.user.repository.IChatRoomLogRepository;
import com.chat.domain.user.repository.IChatRoomRepository;
import com.chat.infrastructure.common.RedisCounter;
import com.chat.infrastructure.common.constant.CacheKey;
import com.chat.infrastructure.po.UmsMember;
import com.chat.infrastructure.util.redis.operator.ListCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @classDesc: 用户创建聊天室发布事件监听
 * @author: cyjer
 * @date: 2023/1/9 11:11
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ChatPublisherEventListener {
    private final ListCache<Long> cache;
    private final IChatRoomLogRepository chatRoomLogRepository;
    private final IChatRoomRepository chatRoomRepository;
    private final RedisCounter redisCounter;

    private static final ExecutorService TASK_EXECUTOR = new ThreadPoolExecutor(30, 50, 5, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1000, false),
            new ThreadFactoryBuilder().setNamePrefix("operatorChatLogThread-").build(),
            new ThreadPoolExecutor.AbortPolicy());

    /**
     * 检查是否还能创建聊天室并减少创建次数
     *
     * @param event
     * @return {@link }
     */
    @EventListener
    public void reduceRoomTicket(CheckAndReduceChatRoomCreateCountEvent event) {
        //减少创建聊天室次数
        cache.pop(CacheKey.getChatRoomTicket().key(event.getUserId()).build(), 1);
    }

    /**
     * 检查是否还能对话并减少次数
     *
     * @param event
     * @return {@link }
     */
    @EventListener
    public void reduceChatTicket(ReduceChatCountEvent event) {
        cache.pop(CacheKey.getChatTicket().key(event.getUserId()).build(), 1);
    }

    /**
     * 记录聊天日志事件,异步,不关注结果
     *
     * @param event
     * @return {@link }
     */
    @EventListener
    public void saveChatLog(SaveChatLogEvent event) {
        TASK_EXECUTOR.submit(() -> {
            chatRoomLogRepository.saveChatRoomLog(event.getChatRoomLog());
        });
    }


    /**
     * 初始化用户对话最大次数、户聊天室最大创建次数
     *
     * @param event
     * @return {@link }
     */
    @EventListener
    public void initChatEventListener(InitChatEvent event) {
        UmsMember umsMember = event.getUmsMember();
        //初始化用户对话可用最大次数
        chatRoomRepository.addChatTicket(String.valueOf(umsMember.getId()), CacheKey.CHAT_TICKET_COUNT);
        //初始化用户聊天室最大创建次数
        chatRoomRepository.addChatTicket(String.valueOf(umsMember.getId()), CacheKey.CHAT_ROOM_COUNT);
    }

}
