package com.cbs.oukasystem.mapstruct.car;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.car.CarRepairEntity;
import com.cbs.oukasystem.mapstruct.car.CarRepairVOEntityMapStruct;
import com.cbs.oukasystem.vo.in.car.IUCarRepairVO;
import com.cbs.oukasystem.vo.out.car.CarRepairVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CarRepairVOEntityMapStruct {
    CarRepairVOEntityMapStruct INSTANCE = Mappers.getMapper(CarRepairVOEntityMapStruct.class);

    CarRepairVO toCarRepairVO(CarRepairEntity entity);

    CarRepairEntity toCarRepairEntity(IUCarRepairVO iVo);

    List<CarRepairVO> toCarRepairVOs(List<CarRepairEntity> entities);
}