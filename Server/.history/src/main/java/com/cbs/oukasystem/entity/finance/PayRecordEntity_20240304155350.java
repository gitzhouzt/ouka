package com.cbs.oukasystem.entity.finance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cbs.oukasystem.common.CommonEnum.EnumStatus;
import com.cbs.oukasystem.common.BusinessEnum.EnumFinanceType;
import com.cbs.oukasystem.entity.OperatorEntity;
import com.cbs.oukasystem.entity.order.OrderEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "finance_pay_record") // JPA-Hibernate
@NoArgsConstructor
@AllArgsConstructor
public class PayRecordEntity extends OperatorEntity {

    @Schema(name = "createByName", description = "記入者名前")
    private String createByName;

    @Schema(name = "auditByName", description = "確認者名前")
    private String auditByName;

    @Schema(name = "orderId")
    private String orderId;

    @Schema(name = "orderNo")
    private String orderNo;

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

    @Schema(name = "expectedAmount", description = "预期日元金额")
    private Double expectedAmount;

    @Schema(name = "amount", description = "实际日元金额")
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
