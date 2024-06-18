package com.cbs.oukasystem.mapstruct.user;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.user.UserDriveEntity;
import com.cbs.oukasystem.vo.in.user.IUUserDriveVO;
import com.cbs.oukasystem.vo.out.user.UserDriveVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDriveVOEntityMapStruct {
        UserDriveVOEntityMapStruct INSTANCE = Mappers.getMapper(UserDriveVOEntityMapStruct.class);

        UserDriveVO toUserDriveVO(UserDriveEntity userEntity);

        UserDriveEntity toUserDriveEntity(IUUserDriveVO iuUserDriveVO);

        IUUserDriveVO toIuUserDriveVO(UserDriveEntity userEntity);

        List<UserDriveVO> toUserDriveVOs(List<UserDriveEntity> userEntities);
}