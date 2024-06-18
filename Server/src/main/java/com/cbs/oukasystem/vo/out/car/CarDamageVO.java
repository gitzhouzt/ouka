package com.cbs.oukasystem.vo.out.car;

import com.cbs.oukasystem.vo.OperatorVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "CarDamageVO") // swagger3
@Data // lombok
@AllArgsConstructor
@NoArgsConstructor
public class CarDamageVO extends OperatorVO {

    @Schema(name = "carId")
    private String carId;

    @Schema(name = "carNo")
    private String carNo;

    @Schema(name = "createByName")
    private String createByName;

    @Schema(name = "auditByName")
    private String auditByName;

    @Schema(name = "carVO")
    private CarVO carVO = new CarVO();

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
