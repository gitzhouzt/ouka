package com.cbs.oukasystem.vo.out.finance;

import com.cbs.oukasystem.common.CommonEnum.EnumStatus;

import java.util.Date;

import com.cbs.oukasystem.common.BusinessEnum.EnumFinanceType;
import com.cbs.oukasystem.vo.OperatorVO;
import com.cbs.oukasystem.vo.out.order.OrderVO;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class PayRecordVO extends OperatorVO {

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

    @Schema(name = "financeTypeName", description = "精算タイプ")
    private String financeTypeName;

    @Schema(name = "amount", description = "日元金额")
    private Double amount;

    @Schema(name = "currencyAmount", description = "币种金额")
    private Double currencyAmount;

    @Schema(name = "currency", description = "币种")
    private String currency;

    @Schema(name = "currencyCode", description = "币种")
    private String currencyCode;

    @Schema(name = "images", description = "写真")
    private String images;

    @Schema(name = "remark", description = "備考")
    private String remark;

    @Schema(name = "status", description = "ステータス")
    private EnumStatus status;

    @Schema(name = "statusName", description = "ステータス")
    private String statusName;

}
