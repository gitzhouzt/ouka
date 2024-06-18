package com.cbs.oukasystem.entity.login;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.common.BusinessEnum.EnumVerifyMethod;
import com.cbs.oukasystem.common.BusinessEnum.EnumVerifyType;
import com.cbs.oukasystem.entity.NormalEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "login_verify") // JPA-Hibernate
public class VerifyEntity extends NormalEntity {

    @Schema(name = "userId")
    private String userId;

    @Schema(name = "verifyType")
    private EnumVerifyType verifyType;

    @Schema(name = "verifyMethod")
    private EnumVerifyMethod verifyMethod;

    @Schema(name = "content")
    private String content;

    @Schema(name = "code")
    private String code;

    @Schema(name = "expiration", description = "有効時間")
    private long expiration;

}
