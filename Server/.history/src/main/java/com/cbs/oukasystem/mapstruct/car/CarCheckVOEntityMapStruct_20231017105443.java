package com.cbs.oukasystem.mapstruct.car;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.car.CarCheckEntity;
import com.cbs.oukasystem.mapstruct.car.CarCheckVOEntityMapStruct;
import com.cbs.oukasystem.vo.in.car.IUCarCheckVO;
import com.cbs.oukasystem.vo.out.car.CarCheckVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CarCheckVOEntityMapStruct {
    CarCheckVOEntityMapStruct INSTANCE = Mappers.getMapper(CarCheckVOEntityMapStruct.class);

    CarCheckVO toCarCheckVO(CarCheckEntity entity);

    CarCheckEntity toCarCheckEntity(IUCarCheckVO iVo);

    List<CarCheckVO> toCarCheckVOs(List<CarCheckEntity> entities);
}