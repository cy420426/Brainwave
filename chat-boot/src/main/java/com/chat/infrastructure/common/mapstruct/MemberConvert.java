package com.chat.infrastructure.common.mapstruct;

import com.chat.domain.user.model.DTO.MemberAuthDTO;
import com.chat.domain.user.model.DO.MemberDO;
import com.chat.domain.user.model.DTO.MemberInfoDTO;
import com.chat.infrastructure.po.UmsMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MemberConvert {
    @Mappings({
            @Mapping(target = "memberId", source = "id"),
            @Mapping(target = "username", source = "openid")
    })
    MemberAuthDTO entity2OpenidAuthDTO(UmsMember entity);

    @Mappings({
            @Mapping(target = "memberId", source = "id"),
            @Mapping(target = "username", source = "mobile")
    })
    MemberAuthDTO entity2MobileAuthDTO(UmsMember entity);

    MemberInfoDTO entity2MemberInfoDTO(UmsMember entity);

    UmsMember dto2Entity(MemberDO memberDO);
}
