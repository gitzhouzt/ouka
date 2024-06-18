package com.cbs.oukasystem.vo.in.order;

import javax.persistence.Column;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUOrderGoodsVO extends NormalVO {
    @Schema(name = "orderId")
    private String orderId;

    @Schema(name = "orderNo")
    private String orderNo;

    @Schema(name = "goodsType", description = "备品类型")
    private String goodsType;

    @Schema(name = "amount", description = "备品数量")
    private int amount;

    @Column(length = 500)
    @Schema(name = "remark", description = "备注")
    private String remark;

}
