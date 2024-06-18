package com.cbs.oukasystem.entity.finance;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cbs.oukasystem.common.BusinessEnum.EnumFinanceStatus;
import com.cbs.oukasystem.entity.OperatorEntity;
import com.cbs.oukasystem.entity.order.OrderEntity;

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

    @Column(name = "order_id", insertable = false, updatable = false, nullable = true)
    @Schema(name = "orderId")
    private String orderId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    @Schema(name = "order")
    private OrderEntity order = new OrderEntity();

    @Schema(name = "orderNo")
    private String orderNo;

    @Schema(name = "handlingFee", description = "手续费")
    private Double handlingFee;

    @Schema(name = "oilFee", description = "油费")
    private Double oilFee;

    @Schema(name = "etc", description = "ETC")
    private Double etc;

    @Schema(name = "entrustSalary", description = "委托工资")
    private Double entrustSalary;

    @Schema(name = "salary", description = "司机工资")
    private Double salary;

    @Schema(name = "entrust", description = "外派金额")
    private Double entrust;

    @Schema(name = "parking", description = "停车场")
    private Double parking;

    @Schema(name = "profit", description = "粗利润")
    private Double profit;

    @Schema(name = "profitRate", description = "粗利润率")
    private Double profitRate;

    @Schema(name = "entrustReason", description = "外派原因")
    private String entrustReason;
}
