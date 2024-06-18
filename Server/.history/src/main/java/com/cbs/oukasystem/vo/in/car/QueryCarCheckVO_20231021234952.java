package com.cbs.oukasystem.vo.in.car;

import com.cbs.oukasystem.common.CommonEnum.EnumStatus;
import com.cbs.oukasystem.vo.QueryVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class QueryCarCheckVO extends QueryVO {

    @Schema(name = "status")
    private EnumStatus status;

}
