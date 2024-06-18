package com.cbs.oukasystem.mapstruct.order;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.order.PayItemEntity;
import com.cbs.oukasystem.vo.in.order.IUPayItemVO;
import com.cbs.oukasystem.vo.out.order.PayItemVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PayItemVOEntityMapStruct {
    PayItemVOEntityMapStruct INSTANCE = Mappers.getMapper(PayItemVOEntityMapStruct.class);

    PayItemVO toVO(PayItemEntity entity);

    PayItemEntity toEntity(IUPayItemVO iVo);

    List<PayItemVO> toVOs(List<PayItemEntity> entities);
}