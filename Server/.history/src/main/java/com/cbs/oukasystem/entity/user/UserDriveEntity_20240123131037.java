package com.cbs.oukasystem.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.common.CommonEnum.EnumSex;
import com.cbs.oukasystem.common.CommonEnum.EnumUserStatus;
import com.cbs.oukasystem.entity.OperatorEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "user_drive") // JPA-Hibernate
public class UserDriveEntity extends OperatorEntity {

    @Schema(name = "userId", description = "Id", example = "userId")
    private String userId;

    @Schema(name = "userNo", description = "番号", example = "example")
    private String userNo;

    @Schema(name = "userName", description = "名前・ワード", example = "example")
    private String userName;

    @Schema(name = "driverType", description = "ドライバータイプ", example = "driverType")
    private String driverType;

}
