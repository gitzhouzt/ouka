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

    @Schema(name = "userNo", description = "番号", example = "example")
    private String userNo;

    @Schema(name = "userNo", description = "番号", example = "example")
    private String userNo;

    @Schema(name = "userName", description = "名前・ワード", example = "example")
    private String userName;

    @Schema(name = "realName")
    private String realName;

    @Schema(name = "userEmail", description = "メール", example = "example@example.com")
    private String userEmail;

    @Schema(name = "countryNum", description = "区番号", example = "81")
    private int countryNum = 0;

    @Schema(name = "userPhone", description = "携帯", example = "0801111222")
    private String userPhone;

    @Schema(name = "userAvatar", description = "アバター")
    private String userAvatar;

    @Schema(name = "userSex", description = " 性別")
    private EnumSex userSex;

    @Schema(name = "userAddress")
    private String userAddress;

    @Schema(name = "userPost")
    private String userPost;

    @Schema(name = "userRoles", description = "役職", example = "STAFF")
    private String userRoles;

    @Schema(name = "driverType", description = "ドライバータイプ", example = "driverType")
    private String driverType;

    @Column(name = "status")
    @Schema(name = "status")
    private EnumUserStatus status = EnumUserStatus.Working;

    @Column(name = "remark")
    @Schema(name = "remark", description = "備考")
    private String remark;

}
