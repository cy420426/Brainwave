package com.chat.domain.assistant.DO;

import com.chat.core.commons.id.IdGenerator;
import com.chat.domain.assistant.BO.WebChatParams;
import com.chat.infrastructure.po.ChatRoom;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @classDesc:
 * @author: cyjer
 * @date: 2023/5/12 14:45
 */
@Data
@ApiModel(value = "小助手")
public class AssistantParams {
    @ApiModelProperty("助理名称")
    private String name;
    @ApiModelProperty("头像地址")
    private String avatar;
    @ApiModelProperty("介绍")
    private String intro;
    @ApiModelProperty("聊天室信息")
    private WebChatParams webChatParams;
    private ChatRoom chatRoom;

    public AssistantParams init() {
        this.setName("base gpt 3.5");
        this.setAvatar("https://img1.baidu.com/it/u=632858062,210522126&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=338");
        this.setIntro("最棒的gpt助手~");
        WebChatParams initParams = new WebChatParams();
        initParams.setChatId(IdGenerator.ins().generator());
        initParams.setInitContent("小助手随时为您服务");
        return this;
    }
}
