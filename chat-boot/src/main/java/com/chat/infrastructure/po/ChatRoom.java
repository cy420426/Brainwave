package com.chat.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

@Data
public class ChatRoom {
    @TableId
    private Long roomId;
    private String userId;
    private String roomName;
    private Date createTime;
    private Date updateTime;
    @TableLogic(delval = "1", value = "0")
    private Integer isDelete;

}
