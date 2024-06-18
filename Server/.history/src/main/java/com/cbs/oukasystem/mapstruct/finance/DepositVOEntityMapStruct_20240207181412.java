package com.cbs.oukasystem.mapstruct.finance;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.finance.DepositEntity;
import com.cbs.oukasystem.vo.in.finance.IUDepositVO;
import com.cbs.oukasystem.vo.out.finance.DepositVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepositVOEntityMapStruct {
        DepositVOEntityMapStruct INSTANCE = Mappers.getMapper(DepositVOEntityMapStruct.class);

        DepositVO toVO(DepositEntity entity);

        DepositEntity toEntity(IUDepositVO iuVo);

        IUDepositVO toIuVO(DepositEntity entity);

        List<DepositVO> toVOs(List<DepositEntity> entities);
}