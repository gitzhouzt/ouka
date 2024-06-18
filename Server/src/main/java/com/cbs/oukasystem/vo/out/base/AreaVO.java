package com.cbs.oukasystem.vo.out.base;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(name = "AreaVO")
@NoArgsConstructor
@AllArgsConstructor
public class AreaVO extends NormalVO {

    @Schema(name = "areaId")
    private String areaId;

    @Schema(name = "areaCode")
    private String areaCode;

    @Schema(name = "areaName")
    private String areaName;

    @Schema(name = "cityCode")
    private String cityCode;

    @Schema(name = "cityName")
    private String cityName;

}
