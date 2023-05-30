package com.chat.domain.user.repository;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chat.domain.user.model.DTO.MemberAuthDTO;
import com.chat.domain.user.model.DO.MemberDO;
import com.chat.domain.user.model.DTO.MemberInfoDTO;
import com.chat.domain.user.model.VO.MemberVO;
import com.chat.infrastructure.po.UmsMember;

public interface IUmsMemberRepository {

    IPage<UmsMember> list(Page<UmsMember> page, String nickname);

    /**
     * 根据 openid 获取会员认证信息
     *
     * @param openid
     * @return
     */
    UmsMember getByOpenid(String openid);

    /**
     * 根据手机号获取会员认证信息
     *
     * @param mobile
     * @return
     */
    MemberAuthDTO getMemberByMobile(String mobile);

    /**
     * 新增会员
     *
     * @param member
     * @return
     */
    Long addMember(MemberDO member);

    /**
     * 获取登录会员信息
     *
     * @return
     */
    MemberVO getCurrMemberInfo();

    /**
     * 修改会员余额
     *
     * @param memberId
     * @param balance  会员余额
     * @return
     */
    boolean updateBalance(Long memberId, Long balance);

    /**
     * 扣减账户余额
     *
     * @param memberId
     * @param amount   扣减金额
     * @return
     */
    boolean deductBalance(Long memberId, Long amount);

    /**
     * 获取会员信息
     *
     * @param memberId
     * @return
     */
    MemberInfoDTO getMemberInfo(Long memberId);


}
