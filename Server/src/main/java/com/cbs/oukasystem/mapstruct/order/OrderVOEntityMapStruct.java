package com.cbs.oukasystem.mapstruct.order;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.order.OrderEntity;
import com.cbs.oukasystem.vo.in.order.IUOrderVO;
import com.cbs.oukasystem.vo.out.order.OrderVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderVOEntityMapStruct {
    OrderVOEntityMapStruct INSTANCE = Mappers.getMapper(OrderVOEntityMapStruct.class);

    OrderVO toVO(OrderEntity entity);

    OrderEntity toEntity(IUOrderVO iVo);

    List<OrderVO> toVOs(List<OrderEntity> entities);
}