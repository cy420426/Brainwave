package com.chat.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chat.domain.assistant.VO.ChatRoomLogVO;

/**
 * ChatGpt
 */
public interface ChatRoomLogApplication {
    /**
     * 检查减少聊天室创建次数
     *
     * @param userId
     * @return {@link }
     */
    void reduceRoomTicket(String userId);
    void checkRoomTicket(String userId);

    Page<ChatRoomLogVO> listChatHistory(String userId, String roomId, Integer current, Integer size);
}
