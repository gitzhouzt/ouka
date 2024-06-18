package com.cbs.oukasystem.vo.in.finance;

import java.util.Date;

import com.cbs.oukasystem.common.BusinessEnum.EnumFinanceStatus;
import com.cbs.oukasystem.entity.order.OrderEntity;
import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUAdvanceVO extends NormalVO {

    @Schema(name = "orderId")
    private String orderId;

    @Schema(name = "order")
    private OrderEntity order = new OrderEntity();

    @Schema(name = "profitAmount", description = "売上総金額")
    private Double profitAmount;

    @Schema(name = "advanceAmount", description = "別請求総金額")
    private Double advanceAmount;

    @Schema(name = "inAmount", description = "入金総金額")
    private Double inAmount;

    @Schema(name = "mealAmount", description = "立替食事代")
    private Double mealAmount;

    @Schema(name = "hotelAmount", description = "立替ホテル代")
    private Double hotelAmount;

    @Schema(name = "bathTaxAmount", description = "入湯税")
    private Double bathTaxAmount;

    @Schema(name = "parkingAmount", description = "駐車代")
    private Double parkingAmount;

    @Schema(name = "etcAmount", description = "ETC料金")
    private Double etcAmount;

    @Schema(name = "roadAmount", description = "有料道路料金")
    private Double roadAmount;

    @Schema(name = "ticketAmount", description = "入門料チケット")
    private Double ticketAmount;

    @Schema(name = "waterAmount", description = "水代")
    private Double waterAmount;

    @Schema(name = "overtimeAmount", description = "タイムアウト")
    private Double overtimeAmount;

    @Schema(name = "otherAmount", description = "その他立替")
    private Double otherAmount;

    @Schema(name = "billingAddress")
    private String billingAddress;

    @Schema(name = "financeByName")
    private Date financeByName;

    @Schema(name = "status")
    private EnumFinanceStatus status;

    @Schema(name = "status")
    private String statusName;

    @Schema(name = "remark")
    private String remark;

}
