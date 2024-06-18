package com.cbs.oukasystem.vo.in.finance;

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
    private int entrustReason;

}
