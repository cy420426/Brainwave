package com.chat.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chat.infrastructure.po.ChatRoomLog;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ChatRoomLogMapper extends BaseMapper<ChatRoomLog> {
}
