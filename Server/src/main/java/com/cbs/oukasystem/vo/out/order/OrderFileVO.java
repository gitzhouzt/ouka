package com.cbs.oukasystem.vo.out.order;

import com.cbs.oukasystem.vo.OperatorVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class OrderFileVO extends OperatorVO {

    @Schema(name = "orderId")
    private String orderId;

    @Schema(name = "orderNo")
    private String orderNo;

    @Schema(name = "fileName")
    private String fileName;

    @Schema(name = "fileUrl")
    private String fileUrl;

    @Schema(name = "share")
    private Boolean share = true;

    @Schema(name = "remark")
    private String remark;

}
