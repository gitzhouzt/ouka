package com.cbs.oukasystem.vo.in.finance;

import com.cbs.oukasystem.common.BusinessEnum.EnumStatus;
import com.cbs.oukasystem.vo.QueryVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class QueryDepositVO extends QueryVO {

    @Schema(name = "status")
    private EnumStatus status;

    @Schema(name = "accidentType")
    private String accidentType;

    @Schema(name = "accidentTypeCode")
    private String accidentTypeCode;
}
