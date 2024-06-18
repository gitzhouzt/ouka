package com.cbs.oukasystem.entity.finance;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cbs.oukasystem.common.CommonEnum.EnumStatus;
import com.cbs.oukasystem.common.BusinessEnum.EnumFinanceType;
import com.cbs.oukasystem.common.BusinessEnum.EnumOrderType;
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

    /*
     * order
     */

    @Schema(name = "orderId")
    private String orderId;

    @Schema(name = "orderNo")
    private String orderNo;

    @Schema(name = "orderSource", description = "订单来源")
    private String orderSource;

    @Schema(name = "orderSourceCode", description = "订单来源Code")
    private String orderSourceCode;

    @Schema(name = "orderType", description = "注文タイプ")
    private EnumOrderType orderType;

    @Schema(name = "startTime", description = "開始日付")
    private Date startTime;

    @Schema(name = "endTime", description = "完了日付 根据选的天数 自动生成")
    private Date endTime;

    @Schema(name = "driverId", description = "運転者Id")
    private String driverId;

    @Schema(name = "driverNo")
    private String driverNo;

    @Schema(name = "driverName", description = "運転者")
    private String driverName;

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
