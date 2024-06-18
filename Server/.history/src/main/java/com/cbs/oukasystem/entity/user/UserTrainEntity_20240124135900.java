package com.cbs.oukasystem.entity.user;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.cbs.oukasystem.entity.NormalEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "user_train") // JPA-Hibernate
public class UserTrainEntity extends NormalEntity {

    @Column(name = "user_id", insertable = false, updatable = false, nullable = true)
    @Schema(name = "userId", description = "Id", example = "userId")
    private String userId;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id")
    @Schema(name = "user", description = "user", example = "user")
    private UserEntity user = new UserEntity();

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

    @Schema(name = "healthyDate", description = "最后一次健康诊断日期")
    private Date healthyDate;
}
