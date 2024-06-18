package com.cbs.oukasystem.vo.in.user;

import com.cbs.oukasystem.vo.QueryVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class QueryUserDriveVO extends QueryVO {

    @Schema(name = "licenseType", description = "運転免許証タイプ")
    private String licenseType;

    @Schema(name = "driverType", description = "ドライバータイプ", example = "driverTypeCode")
    private String driverType;
}
