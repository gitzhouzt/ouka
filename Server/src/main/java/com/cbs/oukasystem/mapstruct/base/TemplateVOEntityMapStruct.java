package com.cbs.oukasystem.mapstruct.base;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.base.TemplateEntity;
import com.cbs.oukasystem.vo.in.base.IUTemplateVO;
import com.cbs.oukasystem.vo.out.base.TemplateVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TemplateVOEntityMapStruct {
        TemplateVOEntityMapStruct INSTANCE = Mappers.getMapper(TemplateVOEntityMapStruct.class);

        TemplateVO toTemplateVO(TemplateEntity dictEntity);

        TemplateEntity toTemplateEntity(IUTemplateVO iuTemplateVO);

        List<TemplateVO> toTemplateVOs(List<TemplateEntity> dictEntities);
}