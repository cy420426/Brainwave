package com.chat.domain.assistant.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("聊天室聊天记录")
public class ChatRoomLogVO {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("聊天室id")
    private String chatRoomId;
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("请求内容")
    private String request;
    @ApiModelProperty("响应内容")
    private String response;
    @ApiModelProperty("请求角色")
    private String reqRole;
    @ApiModelProperty("响应角色")
    private String resRole;

}
