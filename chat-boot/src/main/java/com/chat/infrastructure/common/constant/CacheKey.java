package com.chat.infrastructure.common.constant;

import com.chat.infrastructure.util.redis.key.impl.DefaultCacheKey;

public class CacheKey {
    private static DefaultCacheKey.DefaultCacheKeyBuilder CHAT_ROOM_TICKET = DefaultCacheKey.builder().group("chatRoomTicket");
    private static DefaultCacheKey.DefaultCacheKeyBuilder CHAT_TICKET = DefaultCacheKey.builder().group("chatTicket");
    private static DefaultCacheKey.DefaultCacheKeyBuilder CHAT = DefaultCacheKey.builder().group("chat");
    public static final Integer CHAT_TICKET_COUNT = 100;
    public static final Integer MAX_CHAR = 2000;
    public static final Integer CHAT_ROOM_COUNT = 5;

    public static DefaultCacheKey.DefaultCacheKeyBuilder getChatRoomTicket() {
        return CHAT_ROOM_TICKET;
    }

    public static DefaultCacheKey.DefaultCacheKeyBuilder getChatTicket() {
        return CHAT_TICKET;
    }

    public static DefaultCacheKey.DefaultCacheKeyBuilder getChat() {
        return CHAT;
    }

}
