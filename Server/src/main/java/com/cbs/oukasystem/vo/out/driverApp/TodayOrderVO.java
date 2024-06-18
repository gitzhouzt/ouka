package com.cbs.oukasystem.vo.out.driverApp;

import java.util.Date;
import com.cbs.oukasystem.vo.NormalVO;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(name = "TodayOrderVO")
@NoArgsConstructor
@AllArgsConstructor
public class TodayOrderVO extends NormalVO {

    @Schema(name = "carId")
    private String carId;

    @Schema(name = "carNo", description = "車両番号")
    private String carNo;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "startTime")
    private Date startTime;

    @Schema(name = "orderStatus", description = "订单状态")
    private int orderStatus;

    @Schema(name = "orderNo", description = "订单编号")
    private String orderNo;
}
