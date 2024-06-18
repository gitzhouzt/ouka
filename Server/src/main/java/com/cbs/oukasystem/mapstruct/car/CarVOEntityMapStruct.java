package com.cbs.oukasystem.mapstruct.car;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.car.CarEntity;
import com.cbs.oukasystem.mapstruct.car.CarVOEntityMapStruct;
import com.cbs.oukasystem.vo.in.car.IUCarVO;
import com.cbs.oukasystem.vo.out.car.CarVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CarVOEntityMapStruct {
    CarVOEntityMapStruct INSTANCE = Mappers.getMapper(CarVOEntityMapStruct.class);

    CarVO toVO(CarEntity entity);

    CarEntity toEntity(IUCarVO iVo);

    List<CarVO> toVOs(List<CarEntity> entities);
}