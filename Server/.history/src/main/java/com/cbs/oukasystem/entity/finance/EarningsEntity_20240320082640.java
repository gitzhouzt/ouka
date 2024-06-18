package com.cbs.oukasystem.entity.finance;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.common.BusinessEnum.EnumFinanceStatus;
import com.cbs.oukasystem.entity.OperatorEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "finance_earnings") // JPA-Hibernate
@NoArgsConstructor
@AllArgsConstructor
public class EarningsEntity extends OperatorEntity {

    @Schema(name = "orderId")
    private String orderId;

    @Schema(name = "orderNo")
    private String orderNo;

    @Schema(name = "handlingFee", description = "手续费")
    private int handlingFee;

    @Schema(name = "oilFee", description = "油费")
    private int oilFee;

    @Schema(name = "etc", description = "ETC")
    private int etc;

    @Schema(name = "entrustSalary", description = "委托工资")
    private int entrustSalary;

    @Schema(name = "salary", description = "司机工资")
    private int salary;

    @Schema(name = "entrust", description = "外派金额")
    private int entrust;

    @Schema(name = "parking", description = "停车场")
    private int parking;

    @Schema(name = "profit", description = "粗利润")
    private int profit;

    @Schema(name = "profitRate", description = "粗利润率")
    private int profitRate;

    @Schema(name = "entrustReason", description = "外派原因")
    private int entrustReason;
}
