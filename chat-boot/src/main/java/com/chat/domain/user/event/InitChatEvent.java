package com.chat.domain.user.event;


import com.chat.infrastructure.po.UmsMember;
import org.springframework.context.ApplicationEvent;

/**
 * @classDesc: 聊天初始化事件
 * @author: cyjer
 * @date: 2023/1/9 11:11
 */
public class InitChatEvent extends ApplicationEvent {

    private UmsMember umsMember;

    public InitChatEvent(Object source) {
        super(source);
    }

    public InitChatEvent(Object source, UmsMember umsMember) {
        super(source);
        this.umsMember = umsMember;

    }

    public UmsMember getUmsMember() {
        return umsMember;
    }


}
