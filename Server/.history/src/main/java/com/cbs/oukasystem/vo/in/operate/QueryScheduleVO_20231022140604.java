package com.cbs.oukasystem.vo.in.operate;

import com.cbs.oukasystem.common.BusinessEnum.EnumActionTarget;
import com.cbs.oukasystem.vo.QueryVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class QueryScheduleVO extends QueryVO {

    @Schema(name = "date")
    private String date;

    @Schema(name = "actionTarget")
    private EnumActionTarget actionTarget;
}
