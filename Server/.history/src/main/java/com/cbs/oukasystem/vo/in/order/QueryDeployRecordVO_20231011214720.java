package com.cbs.oukasystem.vo.in.order;

import com.cbs.oukasystem.vo.QueryVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class QueryDeployRecordVO extends QueryVO {

    @Schema(name = "orderId")
    private String orderId;

    @Schema(name = "deployType")
    private String deployType;

}
