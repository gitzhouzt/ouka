package com.cbs.oukasystem.mapstruct.operate;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.operate.AccidentEntity;
import com.cbs.oukasystem.vo.in.operate.IUAccidentVO;
import com.cbs.oukasystem.vo.out.operate.AccidentVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccidentVOEntityMapStruct {
        AccidentVOEntityMapStruct INSTANCE = Mappers.getMapper(AccidentVOEntityMapStruct.class);

        AccidentVO toVO(AccidentEntity entity);

        AccidentEntity toEntity(IUAccidentVO iuVo);

        IUAccidentVO toIuVO(AccidentEntity entity);

        List<AccidentVO> toVOs(List<AccidentEntity> entities);
}