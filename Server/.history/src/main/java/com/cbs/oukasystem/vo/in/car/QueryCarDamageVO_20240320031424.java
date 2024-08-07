package com.cbs.oukasystem.vo.in.car;

import com.cbs.oukasystem.common.BusinessEnum.EnumCarStatus;
import com.cbs.oukasystem.vo.QueryVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class QueryCarDamageVO extends QueryVO {

    @Schema(name = "carId")
    private String carId;
}
