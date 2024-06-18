package com.cbs.oukasystem.mapstruct.login;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.login.VerifyEntity;
import com.cbs.oukasystem.vo.in.login.IUVerifyVO;
import com.cbs.oukasystem.vo.out.login.VerifyVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VerifyVOEntityMapStruct {
    VerifyVOEntityMapStruct INSTANCE = Mappers.getMapper(VerifyVOEntityMapStruct.class);

    VerifyVO toVerifyVO(VerifyEntity verifyEntity);

    VerifyEntity toVerifyEntity(IUVerifyVO iuVerifyVO);

    List<VerifyVO> toVerifyVOs(List<VerifyEntity> verifyEntities);
}