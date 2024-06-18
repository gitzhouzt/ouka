package com.cbs.oukasystem.mapstruct.base;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.base.DictItemEntity;
import com.cbs.oukasystem.vo.in.base.IUDictItemVO;
import com.cbs.oukasystem.vo.out.base.DictItemVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictItemVOEntityMapStruct {
        DictItemVOEntityMapStruct INSTANCE = Mappers.getMapper(DictItemVOEntityMapStruct.class);

        DictItemVO toVO(DictItemEntity entity);

        DictItemEntity toEntity(IUDictItemVO vo);

        List<DictItemVO> toVOs(List<DictItemEntity> entities);
}