package com.cbs.oukasystem.mapstruct.finance;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.finance.AdvanceEntity;
import com.cbs.oukasystem.vo.in.finance.IUAdvanceVO;
import com.cbs.oukasystem.vo.out.finance.AdvanceVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdvanceVOEntityMapStruct {
        AdvanceVOEntityMapStruct INSTANCE = Mappers.getMapper(AdvanceVOEntityMapStruct.class);

        @Mapping(target = "orderVO", source = "order")
        @Mapping(target = "orderNo", source = "order.orderNo")
        @Mapping(target = "orderTypeName", source = "order.orderTypeName")
        @Mapping(target = "startTime", source = "order.startTime")
        @Mapping(target = "endTime", source = "order.endTime")
        @Mapping(target = "sellerName", source = "order.sellerName")
        @Mapping(target = "carNo", source = "order.carNo")
        @Mapping(target = "driverName", source = "order.driverName")
        AdvanceVO toVO(AdvanceEntity entity);

        AdvanceEntity toEntity(IUAdvanceVO iuVo);

        IUAdvanceVO toIuVO(AdvanceEntity entity);

        @Mapping(target = "orderVO", source = "order")
        @Mapping(target = "orderNo", source = "order.orderNo")
        @Mapping(target = "orderTypeName", source = "order.orderTypeName")
        @Mapping(target = "startTime", source = "order.startTime")
        @Mapping(target = "endTime", source = "order.endTime")
        @Mapping(target = "sellerName", source = "order.sellerName")
        @Mapping(target = "carNo", source = "order.carNo")
        @Mapping(target = "driverName", source = "order.driverName")
        List<AdvanceVO> toVOs(List<AdvanceEntity> entities);
}