package com.cbs.oukasystem.vo.in.order;

import java.util.Date;

import com.cbs.oukasystem.common.BusinessEnum.EnumOrderStatus;
import com.cbs.oukasystem.common.BusinessEnum.EnumOrderType;
import com.cbs.oukasystem.vo.NormalVO;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUOrderVO extends NormalVO {

    /*
     * お客さん情報
     */
    @Schema(name = "orderNo", description = "注文番号")
    private String orderNo;

    @Schema(name = "orderSource", description = "注文元")
    private String orderSource;

    @Schema(name = "orderSourceCode", description = "注文元")
    private String orderSourceCode;

    @Schema(name = "orderKey", description = "注文元のかぎ")
    private String orderKey;

    @Schema(name = "city", description = "サービス地域")
    private String city;

    @Schema(name = "orderType", description = "注文タイプ")
    private EnumOrderType orderType;

    @Schema(name = "orderDays", description = "旅行日数")
    private float orderDays = 0;

    @Schema(name = "customerName", description = "お客さん")
    private String customerName;

    @Schema(name = "customerEmail", description = "メール")
    private String customerEmail;

    @Schema(name = "customerPhone", description = "携帯")
    private String customerPhone;

    @Schema(name = "customerWechat", description = "微信")
    private String customerWechat;

    @Schema(name = "emergencyName", description = "紧急联系人")
    private String emergencyName;

    @Schema(name = "emergencyPhone", description = "紧急联系电话")
    private String emergencyPhone;

    @Schema(name = "billingAddress", description = "请求书地址")
    private String billingAddress;

    @Schema(name = "other", description = "その他")
    private String other;

    @Schema(name = "carAccessories", description = "車備品")
    private String carAccessories;

    @Schema(name = "customerRemark", description = "顧客備考")
    private String customerRemark;

    @Schema(name = "companyRemark", description = "会社備考")
    private String companyRemark;

    @Schema(name = "orderStatus", description = "注文ステータス")
    private EnumOrderStatus orderStatus;

    @Schema(name = "orderPrice", description = "注文価格")
    private int orderPrice;

    @Schema(name = "orderFromDetails", description = "出発点 詳細")
    private String orderFromDetails;

    @Schema(name = "orderToDetails", description = "到着点 詳細")
    private String orderToDetails;

    @Schema(name = "flightNo", description = "フライトナンバー（空港送、迎）")
    private String flightNo;

    @Schema(name = "airport", description = "空港（空港送、迎）")
    private String airport;

    /*
     * 車両情報
     */
    @Schema(name = "carId")
    private String carId;

    @Schema(name = "carNo")
    private String carNo;

    @Schema(name = "carName")
    private String carName;

    @Schema(name = "carType")
    private String carType;

    @Schema(name = "specifyCarType", description = "客户指定类型")
    private String specifyCarType;

    @Schema(name = "carSeat")
    private int carSeat = 0;

    /*
     * 運転者情報
     */
    @Schema(name = "driverId", description = "運転者Id")
    private String driverId;

    @Schema(name = "driverNo")
    private String driverNo;

    @Schema(name = "driverName", description = "運転者")
    private String driverName;

    @Schema(name = "driverPhoto", description = "運転者写真")
    private String driverPhoto;

    /*
     * 営業情報
     */
    @Schema(name = "sellerId", description = "営業Id")
    private String sellerId;

    @Schema(name = "sellerNo")
    private String sellerNo;

    @Schema(name = "sellerName", description = "営業")
    private String sellerName;

    @Schema(name = "sellerPhoto", description = "営業写真")
    private String sellerPhoto;

    /**
     * 注文情報
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "startTime", description = "開始日付")
    private Date startTime;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "endTime", description = "完了日付")
    private Date endTime;

    @Schema(name = "adultNum", description = "大人人数")
    private int adultNum = 0;

    @Schema(name = "luggageNum", description = "荷物の数")
    private int luggageNum = 0;

    @Schema(name = "childrenNum", description = "小孩人数")
    private int childrenNum = 0;

}
