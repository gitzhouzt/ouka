package com.cbs.oukasystem.mapstruct.user;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.user.UserHolidayEntity;
import com.cbs.oukasystem.vo.in.user.IUUserHolidayVO;
import com.cbs.oukasystem.vo.out.user.UserHolidayVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserHolidayVOEntityMapStruct {
        UserHolidayVOEntityMapStruct INSTANCE = Mappers.getMapper(UserHolidayVOEntityMapStruct.class);

        UserHolidayVO toVo(UserHolidayEntity entity);

        UserHolidayEntity toEntity(IUUserHolidayVO iuVo);

        IUUserHolidayVO toIuVo(UserHolidayEntity entity);

        List<UserHolidayVO> toVOs(List<UserHolidayEntity> entities);
}