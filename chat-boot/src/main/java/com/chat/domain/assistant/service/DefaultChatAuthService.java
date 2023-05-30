package com.chat.domain.assistant.service;

import com.chat.application.ChatAuthApplication;
import com.chat.infrastructure.common.UserContext;
import com.chat.infrastructure.po.UmsMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class DefaultChatAuthService implements ChatAuthApplication {
    @Override
    public Long getUserId() {
        UmsMember user = UserContext.getUser();
        return Objects.isNull(user) ? 0L : user.getId();
    }

    @Override
    public String getUserName() {
        return ChatAuthApplication.super.getUserName();
    }
}
