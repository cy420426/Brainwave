package com.chat.domain.user.repository;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chat.core.log.annotations.LogForMethod;
import com.chat.infrastructure.po.ChatRoomLog;

public interface IChatRoomLogRepository {

    @LogForMethod("仓储层保存聊天室日志")
    ChatRoomLog saveChatRoomLog(ChatRoomLog chatRoomLog);

    @LogForMethod("仓储层分页查询历史")
    Page<ChatRoomLog> listChatHistory(String userId, String roomId, Integer current, Integer size);

}
