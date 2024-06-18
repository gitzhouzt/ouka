package com.cbs.oukasystem.entity.order;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.common.BusinessEnum.EnumFinanceType;
import com.cbs.oukasystem.entity.OperatorEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "order_pay_record") // JPA-Hibernate
@NoArgsConstructor
@AllArgsConstructor
public class PayRecordEntity extends OperatorEntity {

    /**
     * driver seller
     */

    @Schema(name = "driverId", description = "運転者Id")
    private String driverId;

    @Schema(name = "driverNo")
    private String driverNo;

    @Schema(name = "driverName", description = "運転者")
    private String driverName;

    @Schema(name = "sellerId", description = "営業Id")
    private String sellerId;

    @Schema(name = "sellerNo")
    private String sellerNo;

    @Schema(name = "sellerName", description = "営業")
    private String sellerName;

    @Schema(name = "createByName", description = "記入者名前")
    private String createByName;

    /**
     * order
     */

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

    @Schema(name = "financeType", description = "精算タイプ")
    private EnumFinanceType financeType;

    @Schema(name = "amount", description = "日元金额")
    private Double amount;

    @Schema(name = "currencyAmount", description = "币种金额 日元可不填")
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

}
