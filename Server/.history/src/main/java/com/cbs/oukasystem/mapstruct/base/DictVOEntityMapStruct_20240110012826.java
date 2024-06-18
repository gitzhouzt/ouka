package com.cbs.oukasystem.mapstruct.base;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.base.DictEntity;
import com.cbs.oukasystem.vo.in.base.IUDictVO;
import com.cbs.oukasystem.vo.out.base.DictVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictVOEntityMapStruct {
        DictVOEntityMapStruct INSTANCE = Mappers.getMapper(DictVOEntityMapStruct.class);

        DictVO toVO(DictEntity dictEntity);

        DictEntity toEntity(IUDictVO iuDictVO);

        List<DictVO> toVOs(List<DictEntity> dictEntities);
}