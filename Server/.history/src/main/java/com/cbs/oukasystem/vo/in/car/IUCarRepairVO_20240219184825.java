package com.cbs.oukasystem.vo.in.car;

import java.util.Date;

import com.cbs.oukasystem.common.CommonEnum.EnumStatus;
import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema
@NoArgsConstructor
@AllArgsConstructor
public class IUCarRepairVO extends NormalVO {

    @Schema(name = "carId", description = "車両番号")
    private String carId;

    @Schema(name = "carNo", description = "車両番号")
    private String carNo;

    @Schema(name = "carName", description = "車両名", example = "車両名")
    private String carName;

    @Schema(name = "carRepairType")
    private String repairType;

    @Schema(name = "repairTime")
    private Date repairTime;

    @Schema(name = "status")
    private EnumStatus status = EnumStatus.Filling;

    @Schema(name = "remark", description = "備考")
    private String remark;

}
