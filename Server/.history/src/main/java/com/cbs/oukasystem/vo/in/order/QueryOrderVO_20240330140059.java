package com.cbs.oukasystem.vo.in.order;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.cbs.oukasystem.common.BusinessEnum.EnumOrderStatus;
import com.cbs.oukasystem.common.BusinessEnum.EnumOrderType;
import com.cbs.oukasystem.vo.QueryVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class QueryOrderVO extends QueryVO {

    @Schema(name = "orderStatus", description = "注文ステータス")
    private EnumOrderStatus[] orderStatus;

    @Schema(name = "sellerName")
    private String sellerName;

    @Schema(name = "driverName")
    private String driverName;

    @Schema(name = "carNo")
    private String carNo;

    @Schema(name = "orderType")
    private EnumOrderType orderType;

    @Schema(name = "orderSource")
    private String orderSource;

    @Schema(name = "orderKey")
    private String orderKey;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Schema(name = "startBeginTime")
    private Date startBeginTime;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Schema(name = "startEndTime")
    private Date startEndTime;

}
