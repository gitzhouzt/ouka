package com.cbs.oukasystem.vo.out.driverApp;

import java.util.List;

import com.cbs.oukasystem.vo.NormalVO;
import com.cbs.oukasystem.vo.out.car.CarDamageVO;
import com.cbs.oukasystem.vo.out.car.CarVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(name = "CarDetailsVO")
@NoArgsConstructor
@AllArgsConstructor
public class CarDetailsVO extends NormalVO {

    @Schema(name = "carVO")
    private CarVO carVO = new CarVO();

    @Schema(name = "carDamageVOs")
    private List<CarDamageVO> carDamageVOs;
}
