package com.cbs.oukasystem.mapstruct.finance;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.finance.PayRecordEntity;
import com.cbs.oukasystem.vo.in.finance.IUPayRecordVO;
import com.cbs.oukasystem.vo.out.finance.PayRecordVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PayRecordVOEntityMapStruct {
    PayRecordVOEntityMapStruct INSTANCE = Mappers.getMapper(PayRecordVOEntityMapStruct.class);

    PayRecordVO toVO(PayRecordEntity entity);

    PayRecordEntity toEntity(IUPayRecordVO iVo);

    List<PayRecordVO> toVOs(List<PayRecordEntity> entities);
}