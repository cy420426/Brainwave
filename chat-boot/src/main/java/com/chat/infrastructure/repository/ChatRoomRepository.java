package com.chat.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chat.core.commons.id.IdGenerator;
import com.chat.domain.user.repository.IChatRoomRepository;
import com.chat.infrastructure.common.UserContext;
import com.chat.infrastructure.common.constant.CacheKey;
import com.chat.infrastructure.mapper.ChatRoomMapper;
import com.chat.infrastructure.po.ChatRoom;
import com.chat.infrastructure.util.redis.operator.ListCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @classDesc:
 * @author: cyjer
 * @date: 2023/2/22 17:01
 */
@Component
@RequiredArgsConstructor
public class ChatRoomRepository extends ServiceImpl<ChatRoomMapper, ChatRoom> implements IChatRoomRepository {
    private final ListCache<Long> cache;

    @Override
    public ChatRoom saveChatRoom(ChatRoom chatRoom) {
        if (chatRoom.getRoomId() == null) {
            chatRoom.setRoomId(IdGenerator.ins().generator());
        }
        Long id = UserContext.getUser().getId();
        chatRoom.setUserId(String.valueOf(id));
        boolean success = this.saveOrUpdate(chatRoom);
        return success ? chatRoom : null;
    }

    @Override
    public boolean delChatRoom(String userId, String roomId) {
        return this.remove(new LambdaQueryWrapper<ChatRoom>()
                .eq(ChatRoom::getRoomId, roomId)
                .eq(ChatRoom::getUserId, userId));
    }

    @Override
    public Page<ChatRoom> listRoom(String userId, Integer current, Integer size) {
        Page<ChatRoom> page = new Page<>(current, size);
        return this.page(page, new LambdaQueryWrapper<ChatRoom>()
                .eq(ChatRoom::getUserId, userId)
                .orderByDesc(ChatRoom::getUpdateTime));
    }

    @Override
    public ChatRoom getChatRoomByUserIdAndRoomId(String userId, String roomId) {
        return this.getOne(new LambdaQueryWrapper<ChatRoom>()
                .eq(ChatRoom::getUserId, userId)
                .eq(ChatRoom::getRoomId, roomId));
    }

    @Override
    public void addRoomTicket(String userId, Integer ticketCount) {
        Long[] createRoomCount = new Long[ticketCount];
        Arrays.fill(createRoomCount, 1L);
        List<Long> createRoomCounts = new ArrayList<>(Arrays.asList(createRoomCount));
        cache.save(CacheKey.getChatRoomTicket().key(userId).build(), createRoomCounts, -1L);
    }

    @Override
    public void reduceRoomTicket(String userId, Integer ticketCount) {
        cache.pop(CacheKey.getChatRoomTicket().key(userId).build(), ticketCount);
    }

    @Override
    public void addChatTicket(String userId, Integer ticketCount) {
        Long[] ticket = new Long[ticketCount];
        Arrays.fill(ticket, 1L);
        List<Long> ticketList = new ArrayList<>(Arrays.asList(ticket));
        cache.save(CacheKey.getChatTicket().key(userId).build(), ticketList, -1L);
    }

    @Override
    public void reduceChatTicket(String userId, Integer ticketCount) {
        cache.pop(CacheKey.getChatTicket().key(userId).build(), ticketCount);
    }

    @Override
    public Boolean updateRoomName(Long roomId, String roomName) {
        String userId = UserContext.getUser().getId().toString();
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setUserId(userId);
        chatRoom.setRoomName(roomName);
        chatRoom.setRoomId(roomId);
        return this.update(chatRoom, new LambdaQueryWrapper<ChatRoom>()
                .eq(ChatRoom::getRoomId, roomId)
                .eq(ChatRoom::getUserId, userId));
    }

    @Override
    public Boolean delRoom(Long roomId) {
        return this.remove(new LambdaQueryWrapper<ChatRoom>()
                .eq(ChatRoom::getRoomId, roomId)
                .eq(ChatRoom::getUserId, UserContext.getUser().getId().toString()));
    }
}
