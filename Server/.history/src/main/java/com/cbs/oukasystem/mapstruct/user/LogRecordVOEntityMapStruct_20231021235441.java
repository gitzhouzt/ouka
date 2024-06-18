package com.cbs.oukasystem.mapstruct.user;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.user.UserLogEntity;
import com.cbs.oukasystem.vo.in.user.IUUserLogVO;
import com.cbs.oukasystem.vo.out.user.UserLogVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserLogVOEntityMapStruct {
        UserLogVOEntityMapStruct INSTANCE = Mappers.getMapper(UserLogVOEntityMapStruct.class);

        UserLogVO toVo(UserLogEntity entity);

        UserLogEntity toEntity(IUUserLogVO iuVo);

        IUUserLogVO toIuVo(UserLogEntity entity);

        List<UserLogVO> toVOs(List<UserLogEntity> entities);
}