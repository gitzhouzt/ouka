package com.cbs.oukasystem.mapstruct.user;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.user.UserRestEntity;
import com.cbs.oukasystem.vo.in.user.IUUserRestVO;
import com.cbs.oukasystem.vo.out.user.UserRestVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRestVOEntityMapStruct {
        UserRestVOEntityMapStruct INSTANCE = Mappers.getMapper(UserRestVOEntityMapStruct.class);

        UserRestVO toVo(UserRestEntity entity);

        UserRestEntity toEntity(IUUserRestVO iuVo);

        IUUserRestVO toIuVo(UserRestEntity entity);

        List<UserRestVO> toVOs(List<UserRestEntity> entities);
}