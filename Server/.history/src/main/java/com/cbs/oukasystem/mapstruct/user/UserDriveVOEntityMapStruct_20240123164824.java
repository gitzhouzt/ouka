package com.cbs.oukasystem.mapstruct.user;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.user.UserDriveEntity;
import com.cbs.oukasystem.vo.in.user.IUUserDriveVO;
import com.cbs.oukasystem.vo.out.user.UserDriveVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDriveVOEntityMapStruct {
        UserDriveVOEntityMapStruct INSTANCE = Mappers.getMapper(UserDriveVOEntityMapStruct.class);

        @Mapping(target = "fromVO", source = "from")
        @Mapping(target = "fromVO.companyVO", source = "from.company")
        UserDriveVO toVO(UserDriveEntity entity);

        UserDriveEntity toEntity(IUUserDriveVO iVo);

        IUUserDriveVO toIuVO(UserDriveEntity entity);

        List<UserDriveVO> toVOs(List<UserDriveEntity> entities);
}