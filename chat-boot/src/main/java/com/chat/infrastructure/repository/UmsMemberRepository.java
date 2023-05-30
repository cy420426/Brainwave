package com.chat.infrastructure.repository;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chat.core.commons.code.ElementaryResponseCode;
import com.chat.core.commons.exception.BusinessException;
import com.chat.domain.user.event.InitChatEvent;
import com.chat.domain.user.model.DTO.MemberAuthDTO;
import com.chat.domain.user.model.DO.MemberDO;
import com.chat.domain.user.model.DTO.MemberInfoDTO;
import com.chat.domain.user.model.VO.MemberVO;
import com.chat.domain.user.repository.IUmsMemberRepository;
import com.chat.infrastructure.common.mapstruct.MemberConvert;
import com.chat.infrastructure.common.UserContext;
import com.chat.infrastructure.mapper.UmsMemberMapper;
import com.chat.infrastructure.po.UmsMember;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @classDesc:
 * @author: cyjer
 * @date: 2023/2/22 17:01
 */
@Component
@RequiredArgsConstructor
public class UmsMemberRepository extends ServiceImpl<UmsMemberMapper, UmsMember> implements IUmsMemberRepository {

    @Resource
    private MemberConvert memberConvert;
    @Resource
    private ApplicationEventPublisher publisher;

    @Override
    public IPage<UmsMember> list(Page<UmsMember> page, String nickname) {
        List<UmsMember> list = this.baseMapper.list(page, nickname);
        page.setRecords(list);
        return page;
    }

    /**
     * 根据 openid 获取会员认证信息
     *
     * @param openid
     * @return
     */
    @Override
    public UmsMember getByOpenid(String openid) {
        UmsMember entity = this.getOne(new LambdaQueryWrapper<UmsMember>()
                .eq(UmsMember::getOpenid, openid));
        return entity;
    }

    /**
     * 根据手机号获取会员认证信息
     *
     * @param mobile
     * @return
     */
    @Override
    public MemberAuthDTO getMemberByMobile(String mobile) {
        UmsMember entity = this.getOne(new LambdaQueryWrapper<UmsMember>()
                .eq(UmsMember::getMobile, mobile)
                .select(UmsMember::getId,
                        UmsMember::getMobile,
                        UmsMember::getStatus
                )
        );

        MemberAuthDTO memberAuthDTO = memberConvert.entity2MobileAuthDTO(entity);
        return memberAuthDTO;
    }

    /**
     * 新增会员
     *
     * @param memberDO
     * @return
     */
    @Override
    public Long addMember(MemberDO memberDO) {
        UmsMember umsMember = memberConvert.dto2Entity(memberDO);
        boolean result = this.save(umsMember);
        Optional.of(result)
                .filter(Boolean::booleanValue)
                .orElseThrow(() -> BusinessException.build(ElementaryResponseCode.SYSTEM_ERROR, "新增会员失败"));
        //初始化用户剩余对话次数、聊天室最大创建次数
        publisher.publishEvent(new InitChatEvent(this, umsMember));
        return umsMember.getId();
    }

    /**
     * 获取登录会员信息
     *
     * @return
     */
    @Override
    public MemberVO getCurrMemberInfo() {
        UmsMember umsMember = UserContext.getUser();
        MemberVO memberVO = new MemberVO();
        BeanUtil.copyProperties(umsMember, memberVO);
        return memberVO;
    }

    /**
     * 修改会员余额
     *
     * @param memberId
     * @param balance  会员余额
     * @return
     */
    @Override
    public boolean updateBalance(Long memberId, Long balance) {
        boolean result = this.update(new LambdaUpdateWrapper<UmsMember>()
                .eq(UmsMember::getId, memberId)
                .set(UmsMember::getBalance, balance)
        );
        return result;
    }

    /**
     * 扣减账户余额
     *
     * @param memberId
     * @param amount   扣减金额
     * @return
     */
    @Override
    @Transactional
    public boolean deductBalance(Long memberId, Long amount) {
        boolean result = this.update(new LambdaUpdateWrapper<UmsMember>()
                .setSql("balance = balance - " + amount)
                .eq(UmsMember::getId, memberId)
        );
        return result;
    }

    /**
     * 获取会员信息
     *
     * @param memberId
     * @return
     */
    @Override
    public MemberInfoDTO getMemberInfo(Long memberId) {
        UmsMember entity = this.getById(memberId);
        MemberInfoDTO memberInfoDTO = memberConvert.entity2MemberInfoDTO(entity);
        return memberInfoDTO;
    }
}
