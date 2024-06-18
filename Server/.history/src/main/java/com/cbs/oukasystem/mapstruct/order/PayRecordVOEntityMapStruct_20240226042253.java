package com.cbs.oukasystem.mapstruct.order;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.order.PayRecordEntity;
import com.cbs.oukasystem.vo.in.order.IUPayRecordVO;
import com.cbs.oukasystem.vo.out.order.PayRecordVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PayRecordVOEntityMapStruct {
    PayRecordVOEntityMapStruct INSTANCE = Mappers.getMapper(PayRecordVOEntityMapStruct.class);

    @Mapping(target = "orderVO", source = "order")
    @Mapping(target = "orderNo", source = "order.orderNo")
    PayRecordVO toVO(PayRecordEntity entity);

    PayRecordEntity toEntity(IUPayRecordVO iVo);

    @Mapping(target = "orderVO", source = "order")
    @Mapping(target = "orderNo", source = "order.orderNo")
    List<PayRecordVO> toVOs(List<PayRecordEntity> entities);
}