package com.cbs.oukasystem.mapstruct.finance;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.finance.SettlementEntity;
import com.cbs.oukasystem.vo.in.finance.IUAdvanceVO;
import com.cbs.oukasystem.vo.out.finance.AdvanceVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdvanceVOEntityMapStruct {
        AdvanceVOEntityMapStruct INSTANCE = Mappers.getMapper(AdvanceVOEntityMapStruct.class);

        AdvanceVO toVO(SettlementEntity entity);

        SettlementEntity toEntity(IUAdvanceVO iuVo);

        IUAdvanceVO toIuVO(SettlementEntity entity);

        List<AdvanceVO> toVOs(List<SettlementEntity> entities);
}