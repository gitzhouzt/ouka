package com.cbs.oukasystem.vo.in.finance;

import com.cbs.oukasystem.common.BusinessEnum.EnumFinanceStatus;
import com.cbs.oukasystem.vo.QueryVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class QueryAdvanceVO extends QueryVO {

    @Schema(name = "status")
    private EnumFinanceStatus status;

    @Schema(name = "orderId")
    private String orderId;

    @Schema(name = "sellerName")
    private String sellerName;

    @Schema(name = "orderSource")
    private String orderSource;

}
