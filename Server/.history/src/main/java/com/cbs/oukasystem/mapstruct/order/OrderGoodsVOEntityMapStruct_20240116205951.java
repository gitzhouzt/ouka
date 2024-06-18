package com.cbs.oukasystem.mapstruct.order;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.order.OrderFileEntity;
import com.cbs.oukasystem.vo.in.order.IUOrderFileVO;
import com.cbs.oukasystem.vo.out.order.OrderFileVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderGoodsVOEntityMapStruct {
    OrderGoodsVOEntityMapStruct INSTANCE = Mappers.getMapper(OrderGoodsVOEntityMapStruct.class);

    OrderFileVO toVO(OrderFileEntity entity);

    OrderFileEntity toEntity(IUOrderFileVO iVo);

    List<OrderFileVO> toVOs(List<OrderFileEntity> entities);
}