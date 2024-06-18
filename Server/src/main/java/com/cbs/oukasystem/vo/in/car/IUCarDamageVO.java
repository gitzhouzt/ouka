package com.cbs.oukasystem.vo.in.car;

import com.cbs.oukasystem.entity.car.CarEntity;
import com.cbs.oukasystem.vo.NormalVO;
import com.cbs.oukasystem.vo.out.car.CarVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema
@NoArgsConstructor
@AllArgsConstructor
public class IUCarDamageVO extends NormalVO {

    @Schema(name = "createByName", description = "記入者名前")
    private String createByName;

    @Schema(name = "auditByName", description = "確認者名前")
    private String auditByName;

    @Schema(name = "isAudit", description = "是否確認")
    private Boolean isAudit;

    @Schema(name = "carId")
    private String carId;

    @Schema(name = "car")
    private CarEntity car = new CarEntity();

    @Schema(name = "image1", description = "写真")
    private String image1;

    @Schema(name = "image1", description = "写真")
    private String image2;

    @Schema(name = "image1", description = "写真")
    private String image3;

    @Schema(name = "image1", description = "写真")
    private String image4;

    @Schema(name = "remark", description = "備考")
    private String remark;

}
