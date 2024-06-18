package com.cbs.oukasystem.mapstruct.base;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.base.DictEntity;
import com.cbs.oukasystem.vo.in.base.IUDictVO;
import com.cbs.oukasystem.vo.out.base.DictVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictItemVOEntityMapStruct {
        DictItemVOEntityMapStruct INSTANCE = Mappers.getMapper(DictItemVOEntityMapStruct.class);

        DictVO toDictVO(DictEntity dictEntity);

        DictEntity toDictEntity(IUDictVO iuDictVO);

        List<DictVO> toDictVOs(List<DictEntity> dictEntities);
}