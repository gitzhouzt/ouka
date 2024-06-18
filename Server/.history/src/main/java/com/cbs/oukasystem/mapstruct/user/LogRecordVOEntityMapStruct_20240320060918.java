package com.cbs.oukasystem.mapstruct.user;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.user.LogRecordEntity;
import com.cbs.oukasystem.vo.in.user.IULogRecordVO;
import com.cbs.oukasystem.vo.out.user.LogRecordVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogRecordVOEntityMapStruct {
        LogRecordVOEntityMapStruct INSTANCE = Mappers.getMapper(LogRecordVOEntityMapStruct.class);

        LogRecordVO toVo(LogRecordEntity entity);

        LogRecordEntity toEntity(IULogRecordVO iuVo);

        IULogRecordVO toIuVo(LogRecordEntity entity);

        List<LogRecordVO> toVOs(List<LogRecordEntity> entities);
}