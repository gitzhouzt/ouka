package com.cbs.oukasystem.mapstruct.sys;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.sys.RoleEntity;
import com.cbs.oukasystem.vo.in.sys.IURoleVO;
import com.cbs.oukasystem.vo.out.sys.RoleVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleVOEntityMapStruct {
        RoleVOEntityMapStruct INSTANCE = Mappers.getMapper(RoleVOEntityMapStruct.class);

        RoleVO toRoleVO(RoleEntity roleEntity);

        RoleEntity toRoleEntity(IURoleVO iuRoleVO);

        List<RoleVO> toRoleVOs(List<RoleEntity> roleEntities);
}