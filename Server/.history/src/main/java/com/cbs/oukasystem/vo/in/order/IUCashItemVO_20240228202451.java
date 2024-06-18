package com.cbs.oukasystem.vo.in.order;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUCashItemVO extends NormalVO {

    @Schema(name = "orderId")
    private String orderId;

    @Schema(name = "orderNo")
    private String orderNo;

    @Schema(name = "payItem", description = "收现名目")
    private String payItem;

    @Schema(name = "payItemCode", description = "收现名目")
    private String payItemCode;

    @Schema(name = "amount", description = "日元金额")
    private Double amount;

    @Schema(name = "remark", description = "備考")
    private String remark;

}
