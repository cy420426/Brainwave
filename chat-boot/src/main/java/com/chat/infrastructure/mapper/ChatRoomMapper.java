package com.chat.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chat.infrastructure.po.ChatRoom;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ChatRoomMapper extends BaseMapper<ChatRoom> {
}
