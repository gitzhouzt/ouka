package com.cbs.oukasystem.vo.in.order;

import com.cbs.oukasystem.common.BusinessEnum.EnumFinanceType;
import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUPayRecordVO extends NormalVO {

    @Schema(name = "orderId")
    private String orderId;

    @Schema(name = "orderNo")
    private String orderNo;

    @Schema(name = "payNo")
    private String payNo;

    @Schema(name = "payMethod", description = "支払い方法")
    private String payMethod;

    @Schema(name = "payMethodCode", description = "支払い方法")
    private String payMethodCode;

    @Schema(name = "payItem", description = "支払名目")
    private String payItem;

    @Schema(name = "payItemCode", description = "支払名目")
    private String payItemCode;

    @Schema(name = "financeType", description = "支払いタイプ")
    private EnumFinanceType financeType;

    @Schema(name = "amount", description = "日元金额 日元可不填")
    private Double amount;

    @Schema(name = "currencyAmount", description = "币种金额")
    private Double currencyAmount;

    @Schema(name = "currency", description = "币种")
    private String currency;

    @Schema(name = "currencyCode", description = "币种")
    private String currencyCode;

    @Schema(name = "refundAmount", description = "返金額")
    private String refundAmount;

    @Schema(name = "refundTo", description = "返金相手")
    private String refundTo;

    @Schema(name = "refundTime", description = "返金日付")
    private String refundTime;

    @Schema(name = "remark", description = "備考")
    private String remark;

    @Schema(name = "isAudit", description = "有効審査", example = "有効true 無効true")
    private Boolean isAudit;

}
