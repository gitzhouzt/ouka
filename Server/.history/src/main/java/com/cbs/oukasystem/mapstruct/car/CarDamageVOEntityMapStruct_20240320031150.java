package com.cbs.oukasystem.mapstruct.car;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.car.CarDamageEntity;
import com.cbs.oukasystem.vo.in.car.IUCarDamageVO;
import com.cbs.oukasystem.vo.out.car.CarDamageVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CarDamageVOEntityMapStruct {
    CarDamageVOEntityMapStruct INSTANCE = Mappers.getMapper(CarDamageVOEntityMapStruct.class);

    @Mapping(target = "carVO", source = "car")
    @Mapping(target = "carNo", source = "car.carNo")
    CarDamageVO toVO(CarDamageEntity entity);

    CarDamageEntity toEntity(IUCarDamageVO iVo);

    @Mapping(target = "carVO", source = "car")
    @Mapping(target = "carNo", source = "car.carNo")
    List<CarDamageVO> toVOs(List<CarDamageEntity> entities);
}