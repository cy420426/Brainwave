package com.chat.domain.assistant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chat.application.ChatRoomLogApplication;
import com.chat.core.commons.exception.BusinessException;
import com.chat.domain.assistant.VO.ChatRoomLogVO;
import com.chat.domain.user.event.CheckAndReduceChatRoomCreateCountEvent;
import com.chat.domain.user.repository.IChatRoomLogRepository;
import com.chat.infrastructure.common.constant.CacheKey;
import com.chat.infrastructure.common.mapstruct.ChatConvert;
import com.chat.infrastructure.exception.CommonException;
import com.chat.infrastructure.po.ChatRoomLog;
import com.chat.infrastructure.util.redis.operator.ListCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * OpenAI相关服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomLogService implements ChatRoomLogApplication {
    private final IChatRoomLogRepository chatRoomLogRepository;
    private final ApplicationEventPublisher publisher;
    private final ChatConvert chatConvert;
    private final ListCache<Long> cache;

    @Override
    public void reduceRoomTicket(String userId) {
        publisher.publishEvent(new CheckAndReduceChatRoomCreateCountEvent(this, userId));
    }

    @Override
    public void checkRoomTicket(String userId) {
        Long size = cache.size(CacheKey.getChatRoomTicket().key(userId).build());
        if (size <= 0) {
            throw BusinessException.build(CommonException.ROOM_ERROR);
        }
    }

    @Override
    public Page<ChatRoomLogVO> listChatHistory(String userId, String roomId, Integer current, Integer size) {
        Page<ChatRoomLog> page = chatRoomLogRepository.listChatHistory(userId, roomId, current, size);
        return chatConvert.chatRoomLogPO2VOPage(page);
    }
}
