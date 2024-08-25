package com.cbs.oukasystem.mapstruct.finance;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.finance.EarningsEntity;
import com.cbs.oukasystem.vo.in.finance.IUEarningsVO;
import com.cbs.oukasystem.vo.out.finance.EarningsVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EarningsVOEntityMapStruct {
        EarningsVOEntityMapStruct INSTANCE = Mappers.getMapper(EarningsVOEntityMapStruct.class);

        @Mapping(target = "orderVO", source = "order")
        @Mapping(target = "orderTypeName", source = "order.orderTypeName")
        @Mapping(target = "startTime", source = "order.startTime")
        @Mapping(target = "endTime", source = "order.endTime")
        @Mapping(target = "sellerName", source = "order.sellerName")
        @Mapping(target = "carNo", source = "order.carNo")
        @Mapping(target = "driverName", source = "order.driverName")
        @Mapping(target = "amount", source = "order.amount")
        EarningsVO toVO(EarningsEntity entity);

        EarningsEntity toEntity(IUEarningsVO iuVo);

        IUEarningsVO toIuVO(EarningsEntity entity);

        @Mapping(target = "orderVO", source = "order")
        @Mapping(target = "orderTypeName", source = "order.orderTypeName")
        @Mapping(target = "startTime", source = "order.startTime")
        @Mapping(target = "endTime", source = "order.endTime")
        @Mapping(target = "sellerName", source = "order.sellerName")
        @Mapping(target = "carNo", source = "order.carNo")
        @Mapping(target = "driverName", source = "order.driverName")
        @Mapping(target = "amount", source = "order.amount")
        List<EarningsVO> toVOs(List<EarningsEntity> entities);
}