package com.cbs.oukasystem.vo.in.car;

import com.cbs.oukasystem.common.BusinessEnum.EnumCarStatus;
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

    @Schema(name = "carId")
    private String carId;

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
