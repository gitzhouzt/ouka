package com.cbs.oukasystem.mapstruct.user;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.user.UserEntity;
import com.cbs.oukasystem.vo.in.user.IUUserVO;
import com.cbs.oukasystem.vo.out.user.UserVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserVOEntityMapStruct {
        UserVOEntityMapStruct INSTANCE = Mappers.getMapper(UserVOEntityMapStruct.class);

        @Mapping(target = "loginPass", ignore = true)
        UserVO toVO(UserEntity entity);

        UserEntity toEntity(IUUserVO iVo);

        IUUserVO toIuVO(UserEntity entity);

        @Mapping(target = "loginPass", ignore = true)
        List<UserVO> toVOs(List<UserEntity> entities);
}