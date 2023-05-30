package com.chat.domain.user.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chat.application.MemberApplication;
import com.chat.core.commons.code.error.AdviceErrorCode;
import com.chat.core.commons.exception.BusinessException;
import com.chat.domain.user.model.DO.MemberDO;
import com.chat.domain.user.model.DTO.MemberAuthDTO;
import com.chat.domain.user.model.DTO.MemberInfoDTO;
import com.chat.domain.user.model.VO.MemberVO;
import com.chat.domain.user.repository.IUmsMemberRepository;
import com.chat.infrastructure.common.mapstruct.MemberConvert;
import com.chat.infrastructure.po.UmsMember;
import com.chat.infrastructure.util.JwtTokenUtils;
import com.chat.infrastructure.util.redis.key.impl.DefaultCacheKey;
import com.chat.infrastructure.util.redis.operator.StringCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.chat.infrastructure.util.JwtTokenUtils.TOKEN_EXPIRATION;

/**
 * @classDesc:
 * @author: cyjer
 * @date: 2023/5/15 13:54
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService implements MemberApplication {
    private final IUmsMemberRepository iUmsMemberRepository;
    private final MemberConvert memberConvert;
    private final StringCache stringCache;
    private final WxMaService wxMaService;

    @Override
    public String login(String loginCode, String nickName, String headAvatarUrl) {
//        WxMaJscode2SessionResult sessionInfo;
//        try {
//            sessionInfo = wxMaService.getUserService().getSessionInfo(loginCode);
//        } catch (WxErrorException e) {
//            log.error("调用微信获取openid失败:{}", e.getError());
//            throw BusinessException.build(AdviceErrorCode.OTHER_EX, e.getError().getJson());
//        }
//        String openid = sessionInfo.getOpenid();
        String openid = "1";
        UmsMember umsMember = iUmsMemberRepository.getByOpenid(openid);
        //冻结用户处理
        if (Objects.nonNull(umsMember) && umsMember.getStatus() != 1) {
            throw BusinessException.build(AdviceErrorCode.CONSTRAINT_EX, "系统检测到您的账号异常，已被冻结");
        }
        // 微信用户不存在，注册成为新会员
        MemberDO memberDO = new MemberDO();
        memberDO.setAvatarUrl(headAvatarUrl);
        memberDO.setAvatarUrl(nickName);
        memberDO.setOpenid(openid);
        if (Objects.isNull(umsMember)) {
            Long id = iUmsMemberRepository.addMember(memberDO);
            umsMember = memberConvert.dto2Entity(memberDO);
            umsMember.setId(id);
        }
        //签发token
        String token = JwtTokenUtils.createToken(umsMember);
        stringCache.save(DefaultCacheKey.builder()
                .group("user")
                .key(openid)
                .build(), JSON.toJSONString(umsMember), TOKEN_EXPIRATION, TimeUnit.SECONDS);
        return token;
    }

    @Override
    public IPage<UmsMember> list(Page<UmsMember> page, String nickname) {
        return iUmsMemberRepository.list(page, nickname);
    }

    @Override
    public UmsMember getByOpenid(String openid) {
        return iUmsMemberRepository.getByOpenid(openid);
    }

    @Override
    public MemberAuthDTO getMemberByMobile(String mobile) {
        return iUmsMemberRepository.getMemberByMobile(mobile);
    }

    @Override
    public Long addMember(MemberDO member) {
        return iUmsMemberRepository.addMember(member);
    }

    @Override
    public MemberVO getCurrMemberInfo() {
        return iUmsMemberRepository.getCurrMemberInfo();
    }

    @Override
    public boolean updateBalance(Long memberId, Long balance) {
        return iUmsMemberRepository.updateBalance(memberId, balance);
    }

    @Override
    public boolean deductBalance(Long memberId, Long amount) {
        return iUmsMemberRepository.deductBalance(memberId, amount);
    }

    @Override
    public MemberInfoDTO getMemberInfo(Long memberId) {
        return iUmsMemberRepository.getMemberInfo(memberId);
    }
}
