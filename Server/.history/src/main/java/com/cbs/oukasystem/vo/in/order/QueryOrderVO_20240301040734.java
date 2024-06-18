package com.cbs.oukasystem.vo.in.order;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.cbs.oukasystem.common.BusinessEnum.EnumOrderStatus;
import com.cbs.oukasystem.vo.QueryVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class QueryOrderVO extends QueryVO {

    @Schema(name = "orderStatus", description = "注文ステータス")
    private EnumOrderStatus[] orderStatus;

    @Schema(name = "driverName")
    private String driverName;

    @Schema(name = "carName")
    private String carName;

    @Schema(name = "orderType")
    private String orderType;

    @Schema(name = "orderSource")
    private String orderSource;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Schema(name = "startBeginTime")
    private Date startBeginTime;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Schema(name = "startEndTime")
    private Date startEndTime;

}
