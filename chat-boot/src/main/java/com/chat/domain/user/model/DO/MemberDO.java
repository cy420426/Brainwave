package com.chat.domain.user.model.DO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MemberDO {

    private Integer gender;

    private String nickName;

    private String mobile;

    private LocalDate birthday;

    private String avatarUrl;

    private String openid;

    private String sessionKey;

    private String city;

    private String country;

    private String language;

    private String province;

}
