package com.cbs.oukasystem.vo.out.car;

import java.util.Date;

import com.cbs.oukasystem.common.CommonEnum.EnumStatus;
import com.cbs.oukasystem.vo.OperatorVO;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "CarRepairVO") // swagger3
@Data // lombok
@AllArgsConstructor
@NoArgsConstructor
public class CarRepairVO extends OperatorVO {

    @Schema(name = "carId", description = "車両番号")
    private String carId;

    @Schema(name = "carNo", description = "車両番号")
    private String carNo;

    @Schema(name = "carName", description = "車両名", example = "車両名")
    private String carName;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "carRepairType")
    private String repairType;

    @Schema(name = "repairTime")
    private Date repairTime;

    @Schema(name = "status")
    private EnumStatus status = EnumStatus.Filling;

    @Schema(name = "remark", description = "備考")
    private String remark;

}
