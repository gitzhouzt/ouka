package com.cbs.oukasystem.mapstruct.order;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.order.PayItemEntity;
import com.cbs.oukasystem.vo.in.order.IUCashItemVO;
import com.cbs.oukasystem.vo.out.order.CarItemVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CashItemVOEntityMapStruct {
    CashItemVOEntityMapStruct INSTANCE = Mappers.getMapper(CashItemVOEntityMapStruct.class);

    CarItemVO toVO(PayItemEntity entity);

    PayItemEntity toEntity(IUCashItemVO iVo);

    List<CarItemVO> toVOs(List<PayItemEntity> entities);
}