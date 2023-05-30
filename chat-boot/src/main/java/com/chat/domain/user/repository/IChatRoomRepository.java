package com.chat.domain.user.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chat.core.log.annotations.LogForMethod;
import com.chat.infrastructure.po.ChatRoom;

public interface IChatRoomRepository {

    @LogForMethod("仓储层保存聊天室")
    ChatRoom saveChatRoom(ChatRoom chatRoom);

    @LogForMethod("仓储层根据userId和roomId删除聊天室")
    boolean delChatRoom(String userId, String roomId);

    @LogForMethod("仓储层根据userId分页查询聊天室")
    Page<ChatRoom> listRoom(String userId, Integer current, Integer size);

    @LogForMethod("仓储层根据userId和roomId查询该聊天室")
    ChatRoom getChatRoomByUserIdAndRoomId(String userId, String roomId);

    @LogForMethod("添加聊天室创建次数")
    void addRoomTicket(String userId, Integer ticketCount);

    @LogForMethod("减少聊天室创建次数")
    void reduceRoomTicket(String userId, Integer ticketCount);

    @LogForMethod("添加聊天次数")
    void addChatTicket(String userId, Integer ticketCount);

    @LogForMethod("减少聊天次数")
    void reduceChatTicket(String userId, Integer ticketCount);

    @LogForMethod("仓储层更新房间名")
    Boolean updateRoomName(Long roomId, String roomName);
    @LogForMethod("仓储层删除聊天室")
    Boolean delRoom(Long roomId);
}
