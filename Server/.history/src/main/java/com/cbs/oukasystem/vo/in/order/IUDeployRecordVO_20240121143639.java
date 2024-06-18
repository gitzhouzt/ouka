package com.cbs.oukasystem.vo.in.order;

import com.cbs.oukasystem.common.BusinessEnum.EnumTargetType;
import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUDeployRecordVO extends NormalVO {

    @Schema(name = "orderId")
    private String orderId;

    @Schema(name = "orderNo")
    private String orderNo;

    @Schema(name = "newId")
    private String newId;

    @Schema(name = "newNo")
    private String newNo;

    @Schema(name = "newName")
    private String newName;

    @Schema(name = "targetType")
    private EnumTargetType targetType;

    @Schema(name = "remark", description = "備考")
    private String remark;

}
