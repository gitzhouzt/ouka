package com.cbs.oukasystem.entity.order;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.common.BusinessEnum.EnumOrderStatus;
import com.cbs.oukasystem.common.BusinessEnum.EnumOrderType;
import com.cbs.oukasystem.entity.OperatorEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "order_master") // JPA-Hibernate
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity extends OperatorEntity {

    /*
     * 注文情報
     */
    @Schema(name = "orderNo", description = "注文番号")
    private String orderNo;

    @Schema(name = "orderSource", description = "订单来源")
    private String orderSource;

    @Schema(name = "orderKey", description = "第三方")
    private String orderKey;

    @Schema(name = "city", description = "サービス地域")
    private String city;

    @Schema(name = "orderType", description = "注文タイプ")
    private EnumOrderType orderType;

    @Schema(name = "orderTypeName", description = "注文タイプ")
    private String orderTypeName;

    @Schema(name = "orderPrice", description = "基础价格")
    private int orderPrice;

    @Schema(name = "startTime", description = "開始日付")
    private Date startTime;

    @Schema(name = "orderDays", description = "旅行日数")
    private float orderDays = 0;

    @Schema(name = "endTime", description = "完了日付 根据选的天数 自动生成")
    private Date endTime;

    @Schema(name = "orderStatus", description = "注文ステータス")
    private EnumOrderStatus orderStatus;

    @Schema(name = "orderStatusName", description = "注文ステータス")
    private String orderStatusName;

    @Column(name = "companyRemark", length = 2000)
    @Schema(name = "companyRemark", description = "会社備考")
    private String companyRemark;

    @Schema(name = "orderFrom", description = "出発点")
    private String orderFrom;

    @Schema(name = "orderFromDetails", description = "出発点 詳細")
    private String orderFromDetails;

    @Schema(name = "orderTo", description = "到着点 詳細")
    private String orderTo;

    @Schema(name = "orderToDetails", description = "到着点 詳細")
    private String orderToDetails;

    @Schema(name = "flightNo", description = "フライトナンバー（空港送、迎）")
    private String flightNo;

    @Schema(name = "airport", description = "空港（空港送、迎）")
    private String airport;

    @Schema(name = "feeType", description = "车费类型 全包 高停别")
    private String feeType;

    @Schema(name = "isCash", description = "是否收现，具体名目在支付中填写")
    private Boolean isCash;

    @Schema(name = "isOutTimeCash", description = "超时是否收现")
    private Boolean isOutTimeCash;

    @Schema(name = "outTimeAmount", description = "超时收现金额，每30分钟")
    private int outTimeAmount;

    @Schema(name = "isLodgingTips", description = "是否住宿提醒")
    private Boolean isLodgingTips;

    @Schema(name = "hasETC", description = "ETC料金有无")
    private Boolean hasETC;

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

    /*
     * 営業情報 责任人
     */
    @Schema(name = "sellerId", description = "営業Id")
    private String sellerId;

    @Schema(name = "sellerNo")
    private String sellerNo;

    @Schema(name = "sellerName", description = "営業")
    private String sellerName;

    /*
     * お客さん情報
     */

    @Schema(name = "adultNum", description = "大人人数")
    private int adultNum = 0;

    @Schema(name = "childrenNum", description = "小孩人数")
    private int childrenNum = 0;

    @Schema(name = "luggageNum", description = "荷物数")
    private int luggageNum = 0;

    @Schema(name = "customerName", description = "お客さん")
    private String customerName;

    @Schema(name = "billingAddress", description = "请求书地址")
    private String billingAddress;

    @Column(name = "customerRemark", length = 2000)
    @Schema(name = "customerRemark", description = "顧客備考")
    private String customerRemark;

    @Schema(name = "contactMethod1", description = "連絡方法1")
    private String contactMethod1;

    @Schema(name = "contactContent1", description = "連絡内容1")
    private String contactContent1;

    @Schema(name = "contactMethod2", description = "連絡方法2")
    private String contactMethod2;

    @Schema(name = "contactContent2", description = "連絡内容2")
    private String contactContent2;

    @Schema(name = "contactMethod3", description = "連絡方法3")
    private String contactMethod3;

    @Schema(name = "contactContent3", description = "連絡内容3")
    private String contactContent3;

}
