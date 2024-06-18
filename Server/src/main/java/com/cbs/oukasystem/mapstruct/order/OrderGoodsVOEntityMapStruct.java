package com.cbs.oukasystem.mapstruct.order;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.order.OrderGoodsEntity;
import com.cbs.oukasystem.vo.in.order.IUOrderGoodsVO;
import com.cbs.oukasystem.vo.out.order.OrderGoodsVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderGoodsVOEntityMapStruct {
    OrderGoodsVOEntityMapStruct INSTANCE = Mappers.getMapper(OrderGoodsVOEntityMapStruct.class);

    OrderGoodsVO toVO(OrderGoodsEntity entity);

    OrderGoodsEntity toEntity(IUOrderGoodsVO iVo);

    List<OrderGoodsVO> toVOs(List<OrderGoodsEntity> entities);
}