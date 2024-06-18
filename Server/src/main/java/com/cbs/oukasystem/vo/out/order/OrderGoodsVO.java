package com.cbs.oukasystem.vo.out.order;

import javax.persistence.Column;

import com.cbs.oukasystem.vo.OperatorVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class OrderGoodsVO extends OperatorVO {

    @Schema(name = "orderId")
    private String orderId;

    @Schema(name = "orderNo")
    private String orderNo;

    @Schema(name = "goodsType", description = "备品类型")
    private String goodsType;

    @Schema(name = "goodsTypeCode", description = "备品类型")
    private String goodsTypeCode;

    @Schema(name = "amount", description = "备品数量")
    private int amount;

    @Column(length = 500)
    @Schema(name = "remark", description = "备注")
    private String remark;

}
