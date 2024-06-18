package com.cbs.oukasystem.vo.in.user;

import java.util.Date;

import com.cbs.oukasystem.entity.user.UserEntity;
import com.cbs.oukasystem.vo.NormalVO;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUUserDriveVO extends NormalVO {

    @Schema(name = "userId", description = "Id", example = "userId")
    private String userId;

    @Schema(name = "user", description = "user", example = "user")
    private UserEntity user = new UserEntity();

    @Schema(name = "driverLicense", description = "運転免許証番号")
    private String driverLicense;

    @Schema(name = "licenseType", description = "運転免許証タイプ")
    private String licenseType;

    @Schema(name = "licenseTypeCode", description = "運転免許証タイプ")
    private String licenseTypeCode;

    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+9")
    @Schema(name = "expiryDate", description = "運転免許証 有効期限日")
    private Date expiryDate;

    @Schema(name = "driverType", description = "ドライバータイプ", example = "driverType")
    private String driverType;

    @Schema(name = "driverTypeCode", description = "ドライバータイプ", example = "driverTypeCode")
    private String driverTypeCode;

    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+9")
    @Schema(name = "healthyDate", description = "最后一次健康诊断日期")
    private Date healthyDate;

}
