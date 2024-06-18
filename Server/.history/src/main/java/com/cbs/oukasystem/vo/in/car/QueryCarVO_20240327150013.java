package com.cbs.oukasystem.vo.in.car;

import com.cbs.oukasystem.common.BusinessEnum.EnumCarStatus;
import com.cbs.oukasystem.vo.QueryVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class QueryCarVO extends QueryVO {

    @Schema(name = "carStatus")
    private EnumCarStatus carStatus;

    @Schema(name = "carStatus")
    private EnumCarStatus carStatus;

}
