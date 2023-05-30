package com.chat.domain.user.event;


import com.chat.infrastructure.po.ChatRoomLog;
import org.springframework.context.ApplicationEvent;

/**
 * @classDesc: 聊天室初始化事件
 * @author: cyjer
 * @date: 2023/1/9 11:11
 */
public class SaveChatLogEvent extends ApplicationEvent {

    private ChatRoomLog chatRoomLog;

    public SaveChatLogEvent(Object source) {
        super(source);
    }

    public SaveChatLogEvent(Object source, ChatRoomLog chatRoomLog) {
        super(source);
        this.chatRoomLog = chatRoomLog;
    }

    public ChatRoomLog getChatRoomLog() {
        return chatRoomLog;
    }


}
