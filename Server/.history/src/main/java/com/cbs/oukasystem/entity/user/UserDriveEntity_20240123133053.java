package com.cbs.oukasystem.entity.user;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.entity.NormalEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "user_drive") // JPA-Hibernate
public class UserDriveEntity extends NormalEntity {

    @Schema(name = "userId", description = "Id", example = "userId")
    private String userId;

    @Schema(name = "userNo", description = "番号")
    private String driverNo;

    @Schema(name = "driverName")
    private String driverName;

    @Schema(name = "driverLicense", description = "運転免許証番号")
    private String driverLicense;

    @Schema(name = "licenseType", description = "運転免許証タイプ")
    private String licenseType;

    @Schema(name = "licenseTypeCode", description = "運転免許証タイプ")
    private String licenseTypeCode;

    @Schema(name = "expiryDate", description = "運転免許証 有効期限日")
    private Date expiryDate;

    @Schema(name = "driverType", description = "ドライバータイプ", example = "driverType")
    private String driverType;

    @Schema(name = "driverTypeCode", description = "ドライバータイプ", example = "driverTypeCode")
    private String driverTypeCode;
}
