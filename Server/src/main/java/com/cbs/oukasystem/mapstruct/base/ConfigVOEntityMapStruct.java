package com.cbs.oukasystem.mapstruct.base;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.base.ConfigEntity;
import com.cbs.oukasystem.vo.in.base.IUConfigVO;
import com.cbs.oukasystem.vo.out.base.ConfigVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConfigVOEntityMapStruct {
        ConfigVOEntityMapStruct INSTANCE = Mappers.getMapper(ConfigVOEntityMapStruct.class);

        ConfigVO toConfigVO(ConfigEntity entity);

        ConfigEntity toConfigEntity(IUConfigVO iuConfigVO);

        List<ConfigVO> toConfigVOs(List<ConfigEntity> configEntities);
}