package com.cbs.oukasystem.mapstruct.user;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.user.UserEntity;
import com.cbs.oukasystem.vo.in.user.IUUserVO;
import com.cbs.oukasystem.vo.out.user.UserVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserVOEntityMapStruct {
        UserVOEntityMapStruct INSTANCE = Mappers.getMapper(UserVOEntityMapStruct.class);

        UserVO toUserVO(UserEntity userEntity);

        UserEntity toUserEntity(IUUserVO iuUserVO);

        IUUserVO toIuUserVO(UserEntity userEntity);

        List<UserVO> toUserVOs(List<UserEntity> userEntities);
}