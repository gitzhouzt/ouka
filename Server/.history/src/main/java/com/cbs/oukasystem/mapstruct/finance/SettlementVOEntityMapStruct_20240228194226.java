package com.cbs.oukasystem.mapstruct.finance;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.finance.SettlementEntity;
import com.cbs.oukasystem.vo.in.finance.IUSettlementVO;
import com.cbs.oukasystem.vo.out.finance.SettlementVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SettlementVOEntityMapStruct {
        SettlementVOEntityMapStruct INSTANCE = Mappers.getMapper(SettlementVOEntityMapStruct.class);

        SettlementVO toVO(SettlementEntity entity);

        SettlementEntity toEntity(IUSettlementVO iuVo);

        IUSettlementVO toIuVO(SettlementEntity entity);

        List<SettlementVO> toVOs(List<SettlementEntity> entities);
}