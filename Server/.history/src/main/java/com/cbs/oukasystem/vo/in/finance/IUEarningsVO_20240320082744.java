package com.cbs.oukasystem.vo.in.finance;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.cbs.oukasystem.common.BusinessEnum.EnumFinanceStatus;
import com.cbs.oukasystem.entity.order.OrderEntity;
import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUEarningsVO extends NormalVO {

    @Schema(name = "orderId")
    private String orderId;

    @Schema(name = "order")
    private OrderEntity order = new OrderEntity();

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
