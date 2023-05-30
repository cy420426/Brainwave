package com.chat.controller;

import com.chat.application.MemberApplication;
import com.chat.core.commons.response.ResponseResult;
import com.chat.core.log.annotations.LogForAdaptor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @classDesc:
 * @author: cyjer
 * @date: 2023/2/22 17:40
 */
@RestController
@LogForAdaptor
@Slf4j
@RequiredArgsConstructor
@Api(tags = "登录相关接口")
@RequestMapping("/user")
public class LoginController {
    private final MemberApplication memberApplication;

    @PostMapping("/login")
    @ApiOperation(value = "登录")
    public ResponseResult<String> login(@RequestParam String loginCode,
                                        @RequestParam String nickName,
                                        @RequestParam String headAvatarUrl) {
        String login = memberApplication.login(loginCode, nickName, headAvatarUrl);
        return ResponseResult.success(login);
    }

}
