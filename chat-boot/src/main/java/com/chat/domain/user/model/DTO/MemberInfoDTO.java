package com.chat.domain.user.model.DTO;

import lombok.Data;

@Data
public class MemberInfoDTO {

    private String nickName;

    private String avatarUrl;

    private Long balance;
    private String openid;

}
