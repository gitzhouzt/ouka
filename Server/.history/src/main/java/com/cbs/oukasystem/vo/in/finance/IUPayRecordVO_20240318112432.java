package com.cbs.oukasystem.vo.in.finance;

import com.cbs.oukasystem.common.CommonEnum.EnumStatus;

import com.cbs.oukasystem.common.BusinessEnum.EnumFinanceType;
import com.cbs.oukasystem.entity.order.OrderEntity;
import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUPayRecordVO extends NormalVO {

    @Schema(name = "orderId")
    private String orderId;

    @Schema(name = "order")
    private OrderEntity order = new OrderEntity();

    @Schema(name = "createByName", description = "記入者名前")
    private String createByName;

    @Schema(name = "isAudit", description = "是否確認")
    private Boolean isAudit;

    @Schema(name = "auditByName", description = "確認者名前")
    private String auditByName;

    /**
     * pay record
     */

    @Schema(name = "payMethod", description = "支払い方法")
    private String payMethod;

    @Schema(name = "payMethodCode", description = "支払い方法")
    private String payMethodCode;

    @Schema(name = "bank", description = "金融機関")
    private String bank;

    @Schema(name = "payItem", description = "支払名目")
    private String payItem;

    @Schema(name = "payItemCode", description = "支払名目")
    private String payItemCode;

    @Schema(name = "financeType", description = "精算タイプ")
    private EnumFinanceType financeType;

    @Schema(name = "financeTypeName", description = "精算タイプ")
    private EnumFinanceType financeTypeName;

    @Schema(name = "amount", description = "日元金额")
    private Double amount;

    @Schema(name = "currencyAmount", description = "币种金额")
    private Double currencyAmount;

    @Schema(name = "currency", description = "币种")
    private String currency;

    @Schema(name = "currencyCode", description = "币种")
    private String currencyCode;

    @Schema(name = "remark", description = "備考")
    private String remark;

    @Schema(name = "status", description = "ステータス")
    private EnumStatus status;

}
