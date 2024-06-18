package com.cbs.oukasystem.vo.in.base;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUAreaVO extends NormalVO {

    @Schema(name = "areaCode")
    private String areaCode;

    @Schema(name = "areaName")
    private String areaName;

    @Schema(name = "cityCode")
    private String cityCode;

    @Schema(name = "cityName")
    private String cityName;
}
