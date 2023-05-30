package com.chat.controller;

import com.chat.application.ChatRoomApplication;
import com.chat.application.MemberApplication;
import com.chat.core.commons.code.error.AdviceErrorCode;
import com.chat.core.commons.response.ResponseResult;
import com.chat.domain.user.model.DO.MemberDO;
import com.chat.domain.user.model.DTO.MemberAuthDTO;
import com.chat.domain.user.model.DTO.MemberInfoDTO;
import com.chat.domain.user.model.VO.MemberVO;
import com.chat.infrastructure.common.UserContext;
import com.chat.infrastructure.common.constant.CacheKey;
import com.chat.infrastructure.po.UmsMember;
import com.chat.infrastructure.util.redis.operator.ListCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "会员管理")
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberApplication memberApplication;
    private final ChatRoomApplication chatRoomApplication;
    private final ListCache<Long> cache;

    @ApiOperation(value = "根据会员ID获取openid")
    @GetMapping("/{memberId}")
    public ResponseResult<String> getMemberById(@ApiParam("会员ID") @PathVariable Long memberId) {
        MemberInfoDTO memberInfo = memberApplication.getMemberInfo(memberId);
        String openid = memberInfo.getOpenid();
        return ResponseResult.success(openid);
    }

    @ApiOperation(value = "根据会员ID获取剩余对话次数")
    @GetMapping("/member/chat-ticket-count")
    public ResponseResult<Long> getChatTicketCountById() {
        Long id = UserContext.getUser().getId();
        Long size = cache.size(CacheKey.getChatTicket().key(String.valueOf(id)).build());
        return ResponseResult.success(size);
    }

    @ApiOperation(value = "根据会员ID获取剩余创建聊天室次数")
    @GetMapping("/member/chat-room-count")
    public ResponseResult<Long> getChatRoomTicketCountById() {
        Long id = UserContext.getUser().getId();
        Long size = cache.size(CacheKey.getChatRoomTicket().key(String.valueOf(id)).build());
        return ResponseResult.success(size);
    }

    @ApiOperation(value = "增加对话次数")
    @PostMapping("/member/add-chat-ticket")
    public ResponseResult<Boolean> addTicketCountById(@RequestParam Integer size) {
        Long id = UserContext.getUser().getId();
        chatRoomApplication.addChatTicket(String.valueOf(id), size);
        return ResponseResult.success();
    }

    @ApiOperation(value = "减少对话次数")
    @PostMapping("/member/reduce-chat-ticket")
    public ResponseResult<Boolean> reduceTicketCountById(@RequestParam Integer size) {
        Long id = UserContext.getUser().getId();
        chatRoomApplication.reduceChatTicket(String.valueOf(id), size);
        return ResponseResult.success();
    }

    @ApiOperation(value = "减少创建聊天室次数")
    @PostMapping("/member/reduce-room-ticket")
    public ResponseResult<Boolean> reduceRoomCountById(@RequestParam Integer size) {
        Long id = UserContext.getUser().getId();
        chatRoomApplication.reduceRoomTicket(String.valueOf(id), size);
        return ResponseResult.success();
    }

    @ApiOperation(value = "增加创建聊天室次数")
    @PostMapping("/member/add-room-ticket")
    public ResponseResult<Boolean> addRoomCountById(@RequestParam Integer size) {
        Long id = UserContext.getUser().getId();
        chatRoomApplication.addRoomTicket(String.valueOf(id), size);
        return ResponseResult.success();
    }

    @ApiOperation(value = "新增会员")
    @PostMapping
    public ResponseResult<Long> addMember(@RequestBody MemberDO member) {
        Long memberId = memberApplication.addMember(member);
        return ResponseResult.success(memberId);
    }

    @ApiOperation(value = "获取登录会员信息")
    @GetMapping("/me")
    public ResponseResult<MemberVO> getCurrMemberInfo() {
        MemberVO memberVO = memberApplication.getCurrMemberInfo();
        return ResponseResult.success(memberVO);
    }

    @ApiOperation(value = "扣减会员余额")
    @PutMapping("/{memberId}/balances/_deduct")
    public ResponseResult<Boolean> deductBalance(@PathVariable Long memberId, @RequestParam Long amount) {
        boolean result = memberApplication.deductBalance(memberId, amount);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "根据 openid 获取会员认证信息")
    @GetMapping("/openid/{openid}")
    public ResponseResult<UmsMember> getByOpenid(@ApiParam("微信身份标识") @PathVariable String openid) {
        UmsMember byOpenid = memberApplication.getByOpenid(openid);
        if (byOpenid == null) {
            return ResponseResult.error(AdviceErrorCode.CONSTRAINT_EX);
        }
        return ResponseResult.success(byOpenid);
    }

    @ApiOperation(value = "根据手机号获取会员认证信息", hidden = true)
    @GetMapping("/mobile/{mobile}")
    public ResponseResult<MemberAuthDTO> getMemberByMobile(
            @ApiParam("手机号码") @PathVariable String mobile
    ) {
        MemberAuthDTO memberAuthInfo = memberApplication.getMemberByMobile(mobile);
        if (memberAuthInfo == null) {
            return ResponseResult.error(AdviceErrorCode.CONSTRAINT_EX);
        }
        return ResponseResult.success(memberAuthInfo);
    }


}
