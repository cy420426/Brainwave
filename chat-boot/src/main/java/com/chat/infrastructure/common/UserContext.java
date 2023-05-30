package com.chat.infrastructure.common;

import com.chat.infrastructure.po.UmsMember;


public class UserContext {

    private final static ThreadLocal<UmsMember> LOGIN_USER = new ThreadLocal<>();

    public static void setUser(UmsMember user) {
        LOGIN_USER.set(user);
    }

    public static UmsMember getUser() {
        return LOGIN_USER.get();
    }

    public static void clearUser() {
        LOGIN_USER.remove();
    }

    private UserContext() {

    }
}