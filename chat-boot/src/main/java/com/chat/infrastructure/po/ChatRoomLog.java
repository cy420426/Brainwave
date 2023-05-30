package com.chat.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

@Data
public class ChatRoomLog {
    @TableId
    private Long id;
    private String chatRoomId;
    private String userId;
    private String request;
    private String response;
    private String reqRole;
    private String resRole;
    private Date createTime;
    private Date updateTime;
    @TableLogic(delval = "1", value = "0")
    private Integer isDelete;

}
