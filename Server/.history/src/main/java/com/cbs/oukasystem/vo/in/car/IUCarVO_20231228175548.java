package com.cbs.oukasystem.vo.in.car;

import com.cbs.oukasystem.common.BusinessEnum.EnumCarStatus;
import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema
@NoArgsConstructor
@AllArgsConstructor
public class IUCarVO extends NormalVO {

    /*
     * 車両
     */

    @Schema(name = "carNo", description = "車両番号")
    private String carNo;

    @Schema(name = "carName", description = "車両名", example = "車両名")
    private String carName;

    @Schema(name = "carPhoto", description = "車両写真")
    private String carPhoto;

    @Schema(name = "carType")
    private String carType;

    @Schema(name = "carSeat")
    private int carSeat = 0;

    @Schema(name = "city")
    private String city;

    @Schema(name = "plateNum")
    private String plateNum;

    @Schema(name = "carPark")
    private String carPark;

    @Schema(name = "parkingFee")
    private int parkingFee;

    @Schema(name = "status")
    private EnumCarStatus status = EnumCarStatus.Standing;

    @Schema(name = "remark", description = "備考")
    private String remark;

}
