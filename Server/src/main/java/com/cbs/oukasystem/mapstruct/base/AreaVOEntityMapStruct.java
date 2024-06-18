package com.cbs.oukasystem.mapstruct.base;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.base.AreaEntity;
import com.cbs.oukasystem.vo.in.base.IUAreaVO;
import com.cbs.oukasystem.vo.out.base.AreaVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AreaVOEntityMapStruct {

        AreaVOEntityMapStruct INSTANCE = Mappers.getMapper(AreaVOEntityMapStruct.class);

        AreaVO toAreaVO(AreaEntity areaEntity);

        AreaEntity toAreaEntity(IUAreaVO iuAreaVO);

        List<AreaVO> toAreaVOs(List<AreaEntity> areaEntities);
}