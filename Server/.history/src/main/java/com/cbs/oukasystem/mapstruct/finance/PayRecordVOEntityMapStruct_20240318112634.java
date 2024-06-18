package com.cbs.oukasystem.mapstruct.finance;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.finance.PayRecordEntity;
import com.cbs.oukasystem.vo.in.finance.IUPayRecordVO;
import com.cbs.oukasystem.vo.out.finance.PayRecordVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PayRecordVOEntityMapStruct {
    PayRecordVOEntityMapStruct INSTANCE = Mappers.getMapper(PayRecordVOEntityMapStruct.class);

    @Mapping(target = "orderVO", source = "order")
    @Mapping(target = "orderNo", source = "order.orderNo")
    @Mapping(target = "orderTypeName", source = "order.orderTypeName")
    @Mapping(target = "startTime", source = "order.startTime")
    @Mapping(target = "endTime", source = "order.endTime")
    @Mapping(target = "sellerName", source = "order.sellerName")
    @Mapping(target = "carNo", source = "order.carNo")
    @Mapping(target = "driverName", source = "order.driverName")
    PayRecordVO toVO(PayRecordEntity entity);

    PayRecordEntity toEntity(IUPayRecordVO iVo);

    @Mapping(target = "orderVO", source = "order")
    @Mapping(target = "orderNo", source = "order.orderNo")
    @Mapping(target = "orderTypeName", source = "order.orderTypeName")
    @Mapping(target = "startTime", source = "order.startTime")
    @Mapping(target = "endTime", source = "order.endTime")
    @Mapping(target = "sellerName", source = "order.sellerName")
    @Mapping(target = "carNo", source = "order.carNo")
    @Mapping(target = "driverName", source = "order.driverName")
    List<PayRecordVO> toVOs(List<PayRecordEntity> entities);
}