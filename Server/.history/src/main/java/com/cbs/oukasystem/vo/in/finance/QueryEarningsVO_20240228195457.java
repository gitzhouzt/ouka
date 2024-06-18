package com.cbs.oukasystem.vo.in.finance;

import com.cbs.oukasystem.common.BusinessEnum.EnumFinanceStatus;
import com.cbs.oukasystem.vo.QueryVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class QueryEarningsVO extends QueryVO {

    @Schema(name = "status")
    private EnumFinanceStatus status;

    @Schema(name = "orderId")
    private String orderId;
}
