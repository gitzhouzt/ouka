package com.cbs.oukasystem.mapstruct.user;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.cbs.oukasystem.entity.user.UserTrainEntity;
import com.cbs.oukasystem.vo.in.user.IUUserTrainVO;
import com.cbs.oukasystem.vo.out.user.UserTrainVO;

import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserTrainVOEntityMapStruct {
        UserTrainVOEntityMapStruct INSTANCE = Mappers.getMapper(UserTrainVOEntityMapStruct.class);

        @Mapping(target = "userVO", source = "user")
        UserTrainVO toVO(UserTrainEntity entity);

        UserTrainEntity toEntity(IUUserTrainVO iVo);

        IUUserTrainVO toIuVO(UserTrainEntity entity);

        @Mapping(target = "userVO", source = "user")
        List<UserTrainVO> toVOs(List<UserTrainEntity> entities);
}