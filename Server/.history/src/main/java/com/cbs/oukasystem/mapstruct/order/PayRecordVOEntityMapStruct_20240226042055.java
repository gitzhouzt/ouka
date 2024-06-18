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

    @Mapping(target = "userVO", source = "user")
    @Mapping(target = "userName", source = "user.userName")
    @Mapping(target = "userNo", source = "user.userNo")
    PayRecordVO toVO(PayRecordEntity entity);

    PayRecordEntity toEntity(IUPayRecordVO iVo);

    List<PayRecordVO> toVOs(List<PayRecordEntity> entities);
}