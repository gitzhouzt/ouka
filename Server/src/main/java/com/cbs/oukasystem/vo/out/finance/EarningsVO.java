package com.cbs.oukasystem.vo.out.finance;

import java.util.Date;

import com.cbs.oukasystem.vo.OperatorVO;
import com.cbs.oukasystem.vo.out.order.OrderVO;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "EarningsVO") // swagger3
@Data // lombok
@AllArgsConstructor
@NoArgsConstructor
public class EarningsVO extends OperatorVO {
    @Schema(name = "createByName", description = "記入者名前")
    private String createByName;

    @Schema(name = "auditByName", description = "確認者名前")
    private String auditByName;

    @Schema(name = "orderVO")
    private OrderVO orderVO = new OrderVO();

    @Schema(name = "orderId")
    private String orderId;

    @Schema(name = "orderNo", description = "orderNo")
    private String orderNo;

    @Schema(name = "amount", description = "基本料金")
    private Double amount;

    @Schema(name = "orderTypeName", description = "注文タイプ")
    private String orderTypeName;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "startTime", description = "開始日付")
    private Date startTime;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "endTime", description = "終了日付")
    private Date endTime;

    @Schema(name = "sellerName", description = "責任者")
    private String sellerName;

    @Schema(name = "carNo")
    private String carNo;

    @Schema(name = "driverName", description = "運転者")
    private String driverName;

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
