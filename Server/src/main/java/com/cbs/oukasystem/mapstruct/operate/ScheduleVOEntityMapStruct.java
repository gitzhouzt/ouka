package com.cbs.oukasystem.mapstruct.operate;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.operate.ScheduleEntity;
import com.cbs.oukasystem.mapstruct.operate.ScheduleVOEntityMapStruct;
import com.cbs.oukasystem.vo.in.operate.IUScheduleVO;
import com.cbs.oukasystem.vo.out.operate.ScheduleVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduleVOEntityMapStruct {
    ScheduleVOEntityMapStruct INSTANCE = Mappers.getMapper(ScheduleVOEntityMapStruct.class);

    ScheduleVO toScheduleVO(ScheduleEntity scheduleEntity);

    ScheduleEntity toScheduleEntity(IUScheduleVO iuScheduleVO);

    List<ScheduleVO> toScheduleVOs(List<ScheduleEntity> scheduleEntities);
}