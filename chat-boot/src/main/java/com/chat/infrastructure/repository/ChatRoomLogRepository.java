package com.chat.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chat.domain.assistant.VO.ChatRoomLogVO;
import com.chat.domain.user.repository.IChatRoomLogRepository;
import com.chat.infrastructure.common.mapstruct.ChatConvert;
import com.chat.infrastructure.mapper.ChatRoomLogMapper;
import com.chat.infrastructure.po.ChatRoomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @classDesc:
 * @author: cyjer
 * @date: 2023/2/22 17:01
 */
@Component
@RequiredArgsConstructor
public class ChatRoomLogRepository extends ServiceImpl<ChatRoomLogMapper, ChatRoomLog> implements IChatRoomLogRepository {

    @Override
    public ChatRoomLog saveChatRoomLog(ChatRoomLog chatRoomLog) {
        boolean save = this.save(chatRoomLog);
        return save ? chatRoomLog : null;
    }

    @Override
    public Page<ChatRoomLog> listChatHistory(String userId, String roomId, Integer current, Integer size) {
        Page<ChatRoomLog> page = new Page<>(current, size);
        return this.page(page, new LambdaQueryWrapper<ChatRoomLog>()
                .eq(ChatRoomLog::getChatRoomId, roomId)
                .eq(ChatRoomLog::getUserId, userId)
                .orderByDesc(ChatRoomLog::getUpdateTime));
    }
}
