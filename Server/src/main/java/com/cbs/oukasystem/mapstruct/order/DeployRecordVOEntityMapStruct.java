package com.cbs.oukasystem.mapstruct.order;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.order.DeployRecordEntity;
import com.cbs.oukasystem.vo.in.order.IUDeployRecordVO;
import com.cbs.oukasystem.vo.out.order.DeployRecordVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeployRecordVOEntityMapStruct {
    DeployRecordVOEntityMapStruct INSTANCE = Mappers.getMapper(DeployRecordVOEntityMapStruct.class);

    DeployRecordVO toVO(DeployRecordEntity entity);

    DeployRecordEntity toEntity(IUDeployRecordVO iVo);

    List<DeployRecordVO> toVOs(List<DeployRecordEntity> entities);
}