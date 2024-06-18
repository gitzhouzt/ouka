package com.cbs.oukasystem.mapstruct.sys;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.sys.MenuEntity;
import com.cbs.oukasystem.vo.in.sys.IUMenuVO;
import com.cbs.oukasystem.vo.out.sys.MenuVO;

@Mapper
public interface MenuVOEntityMapStruct {
    MenuVOEntityMapStruct INSTANCE = Mappers.getMapper(MenuVOEntityMapStruct.class);

    MenuVO toMenuVO(MenuEntity menuEntity);

    MenuEntity toMenuEntity(IUMenuVO iuMenuVO);

    List<MenuVO> toMenuVOs(List<MenuEntity> menuEntities);
}