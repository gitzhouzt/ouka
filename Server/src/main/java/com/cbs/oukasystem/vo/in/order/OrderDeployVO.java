package com.cbs.oukasystem.vo.in.order;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class OrderDeployVO extends NormalVO {

    @Schema(name = "orderId")
    private String orderId;

    @Schema(name = "orderNo")
    private String orderNo;

    @Schema(name = "driverId", description = "運転者Id")
    private String driverId;

    @Schema(name = "driverNo")
    private String driverNo;

    @Schema(name = "driverName", description = "運転者")
    private String driverName;

    @Schema(name = "driverPhoto", description = "運転者写真")
    private String driverPhoto;

    @Schema(name = "driverRemark")
    private String driverRemark;

    @Schema(name = "carId")
    private String carId;

    @Schema(name = "carNo")
    private String carNo;

    @Schema(name = "carName")
    private String carName;

    @Schema(name = "carType")
    private String carType;

    @Schema(name = "carSeat")
    private int carSeat;

    @Schema(name = "carRemark")
    private String carRemark;
}
