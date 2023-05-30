package com.chat.domain.assistant.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 聊天初始化参数
 */
@Data
@ApiModel(value = "网页聊天参数")
public class WebChatParams {
    @ApiModelProperty("会话ID")
    Long chatId;

    @ApiModelProperty("初始化内容")
    String initContent;

}
