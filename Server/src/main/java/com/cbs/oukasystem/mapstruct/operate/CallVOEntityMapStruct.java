package com.cbs.oukasystem.mapstruct.operate;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.operate.CallEntity;
import com.cbs.oukasystem.vo.in.operate.IUCallVO;
import com.cbs.oukasystem.vo.out.operate.CallVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CallVOEntityMapStruct {
        CallVOEntityMapStruct INSTANCE = Mappers.getMapper(CallVOEntityMapStruct.class);

        CallVO toVO(CallEntity entity);

        CallEntity toEntity(IUCallVO iuVo);

        IUCallVO toIuVO(CallEntity entity);

        List<CallVO> toVOs(List<CallEntity> entities);
}