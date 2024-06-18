package com.cbs.oukasystem.mapstruct.finance;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.finance.EarningsEntity;
import com.cbs.oukasystem.vo.in.finance.IUEarningsVO;
import com.cbs.oukasystem.vo.out.finance.EarningsVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EarningsVOEntityMapStruct {
        EarningsVOEntityMapStruct INSTANCE = Mappers.getMapper(EarningsVOEntityMapStruct.class);

        EarningsVO toVO(EarningsEntity entity);

        EarningsEntity toEntity(IUEarningsVO iuVo);

        IUEarningsVO toIuVO(EarningsEntity entity);

        List<EarningsVO> toVOs(List<EarningsEntity> entities);
}