package com.chat.domain.assistant.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "聊天室列表")
public class ChatRoomVO {
    @ApiModelProperty("聊天室名称")
    private String roomName;
    @ApiModelProperty("聊天室id")
    private Long roomId;
}
