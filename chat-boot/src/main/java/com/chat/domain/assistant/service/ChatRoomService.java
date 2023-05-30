package com.chat.domain.assistant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chat.application.ChatRoomApplication;
import com.chat.domain.assistant.VO.ChatRoomVO;
import com.chat.domain.user.repository.IChatRoomRepository;
import com.chat.infrastructure.common.UserContext;
import com.chat.infrastructure.common.mapstruct.ChatConvert;
import com.chat.infrastructure.po.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * OpenAI相关服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService implements ChatRoomApplication {
    private final IChatRoomRepository chatRoomRepository;
    private final ChatConvert chatConvert;

    @Override
    public ChatRoom saveChatRoom(ChatRoomVO chatRoomVO) {
        ChatRoom chatRoom = chatConvert.chatRoomVO2PO(chatRoomVO);
        return chatRoomRepository.saveChatRoom(chatRoom);
    }

    @Override
    public boolean delChatRoom(String userId, String roomId) {
        return chatRoomRepository.delChatRoom(userId, roomId);
    }

    @Override
    public boolean existsChatRoom(String userId, String roomId) {
        ChatRoom room = chatRoomRepository.getChatRoomByUserIdAndRoomId(userId, roomId);
        return Objects.nonNull(room);
    }

    @Override
    public Page<ChatRoomVO> listRoom(String userId, Integer current, Integer size) {
        Page<ChatRoom> chatRoomPage = chatRoomRepository.listRoom(userId, current, size);
        return chatConvert.chatRoomPO2VOPage(chatRoomPage);
    }

    @Override
    public void addRoomTicket(String userId, Integer ticketCount) {
        chatRoomRepository.addRoomTicket(userId, ticketCount);
    }

    @Override
    public void reduceRoomTicket(String userId, Integer ticketCount) {
        chatRoomRepository.reduceRoomTicket(userId, ticketCount);
    }

    @Override
    public void addChatTicket(String userId, Integer ticketCount) {
        chatRoomRepository.addChatTicket(userId, ticketCount);
    }

    @Override
    public void reduceChatTicket(String userId, Integer ticketCount) {
        chatRoomRepository.reduceChatTicket(userId, ticketCount);
    }

    @Override
    public Boolean updateRoomName(Long roomId, String roomName) {
        return chatRoomRepository.updateRoomName(roomId, roomName);
    }

    @Override
    public Boolean delRoom(Long roomId) {
        Boolean success = chatRoomRepository.delRoom(roomId);
        chatRoomRepository.addRoomTicket(UserContext.getUser().getId().toString(), 1);
        return success;
    }
}
