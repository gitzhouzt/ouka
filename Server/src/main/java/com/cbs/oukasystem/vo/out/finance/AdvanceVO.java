package com.cbs.oukasystem.vo.out.finance;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.cbs.oukasystem.common.BusinessEnum.EnumFinanceStatus;
import com.cbs.oukasystem.entity.order.OrderEntity;
import com.cbs.oukasystem.vo.OperatorVO;
import com.cbs.oukasystem.vo.out.order.OrderVO;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "AdvanceVO") // swagger3
@Data // lombok
@AllArgsConstructor
@NoArgsConstructor
public class AdvanceVO extends OperatorVO {

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

    @Schema(name = "profitAmount", description = "売上総金額")
    private Double profitAmount = 0.0;

    @Schema(name = "advanceAmount", description = "別請求総金額")
    private Double advanceAmount = 0.0;

    @Schema(name = "inAmount", description = "入金総金額")
    private Double inAmount = 0.0;

    @Schema(name = "mealAmount", description = "立替食事代")
    private Double mealAmount = 0.0;

    @Schema(name = "hotelAmount", description = "立替ホテル代")
    private Double hotelAmount = 0.0;

    @Schema(name = "bathTaxAmount", description = "入湯税")
    private Double bathTaxAmount = 0.0;

    @Schema(name = "parkingAmount", description = "駐車代")
    private Double parkingAmount = 0.0;

    @Schema(name = "etcAmount", description = "ETC料金")
    private Double etcAmount = 0.0;

    @Schema(name = "roadAmount", description = "有料道路料金")
    private Double roadAmount = 0.0;

    @Schema(name = "ticketAmount", description = "入門料チケット")
    private Double ticketAmount = 0.0;

    @Schema(name = "waterAmount", description = "水代")
    private Double waterAmount = 0.0;

    @Schema(name = "overtimeAmount", description = "タイムアウト")
    private Double overtimeAmount = 0.0;

    @Schema(name = "otherAmount", description = "その他立替")
    private Double otherAmount = 0.0;

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
