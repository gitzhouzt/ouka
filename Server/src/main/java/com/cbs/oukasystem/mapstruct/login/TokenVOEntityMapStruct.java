package com.cbs.oukasystem.mapstruct.login;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.login.TokenEntity;
import com.cbs.oukasystem.vo.out.login.TokenVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TokenVOEntityMapStruct {
    TokenVOEntityMapStruct INSTANCE = Mappers.getMapper(TokenVOEntityMapStruct.class);

    TokenVO toTokenVO(TokenEntity tokenEntity);
}