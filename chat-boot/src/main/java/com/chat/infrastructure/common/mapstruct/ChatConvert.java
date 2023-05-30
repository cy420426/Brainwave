package com.chat.infrastructure.common.mapstruct;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chat.domain.assistant.VO.ChatRoomLogVO;
import com.chat.domain.assistant.VO.ChatRoomVO;
import com.chat.infrastructure.po.ChatRoom;
import com.chat.infrastructure.po.ChatRoomLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatConvert {
    ChatRoom chatRoomVO2PO(ChatRoomVO chatRoomVO);
    ChatRoomLogVO chatRoomLogPO2VO(ChatRoomLog chatRoomLog);
    ChatRoomLog chatRoomLogVO2PO(ChatRoomLogVO chatRoomLogVO);
    Page<ChatRoomLog>  chatRoomLogVO2POPage(Page<ChatRoomLogVO> chatRoomLogVOPage);
    Page<ChatRoomLogVO>  chatRoomLogPO2VOPage(Page<ChatRoomLog> chatRoomLogPage);

    Page<ChatRoomVO> chatRoomPO2VOPage(Page<ChatRoom> chatRoom);
}
