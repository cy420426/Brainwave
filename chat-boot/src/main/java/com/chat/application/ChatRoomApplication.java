package com.chat.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chat.core.log.annotations.LogForMethod;
import com.chat.domain.assistant.VO.ChatRoomVO;
import com.chat.infrastructure.po.ChatRoom;

/**
 * ChatGpt
 */
public interface ChatRoomApplication {
    ChatRoom saveChatRoom(ChatRoomVO chatRoomVO);

    boolean delChatRoom(String userId, String roomId);
    boolean existsChatRoom(String userId, String roomId);

    Page<ChatRoomVO> listRoom(String userId, Integer current, Integer size);

    void addRoomTicket(String userId, Integer ticketCount);

    void reduceRoomTicket(String userId, Integer ticketCount);

    void addChatTicket(String userId, Integer ticketCount);

    void reduceChatTicket(String userId, Integer ticketCount);

    Boolean updateRoomName(Long roomId, String roomName);
    Boolean delRoom(Long roomId);
}
