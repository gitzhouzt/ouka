package com.cbs.oukasystem.mapstruct.order;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.order.OrderFileEntity;
import com.cbs.oukasystem.vo.in.order.IUOrderVO;
import com.cbs.oukasystem.vo.out.order.OrderFileVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderFileVOEntityMapStruct {
    OrderFileVOEntityMapStruct INSTANCE = Mappers.getMapper(OrderFileVOEntityMapStruct.class);

    OrderFileVO toOrderVO(OrderFileEntity entity);

    OrderFileEntity toOrderEntity(IUOrderVO iVo);

    List<OrderVO> toOrderVOs(List<OrderFileEntity> entities);
}