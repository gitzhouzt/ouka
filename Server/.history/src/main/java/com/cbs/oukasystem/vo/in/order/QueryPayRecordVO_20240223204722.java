package com.cbs.oukasystem.vo.in.order;

import com.cbs.oukasystem.common.BusinessEnum.EnumFinanceType;
import com.cbs.oukasystem.vo.QueryVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class QueryPayRecordVO extends QueryVO {

    @Schema(name = "orderId")
    private String orderId;

    @Schema(name = "payMethod")
    private String payMethod;

    @Schema(name = "payItem")
    private String payItem;

    @Schema(name = "financeType")
    private EnumFinanceType financeType;
}
