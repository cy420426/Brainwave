package com.chat.domain.user.event;


import org.springframework.context.ApplicationEvent;

/**
 * @classDesc: 聊天室初始化事件
 * @author: cyjer
 * @date: 2023/1/9 11:11
 */
public class CheckAndReduceChatRoomCreateCountEvent extends ApplicationEvent {

    private String userId;

    public CheckAndReduceChatRoomCreateCountEvent(Object source) {
        super(source);
    }

    public CheckAndReduceChatRoomCreateCountEvent(Object source, String userId) {
        super(source);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }


}
