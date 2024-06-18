package com.cbs.oukasystem.vo.in.login;

import com.cbs.oukasystem.common.BusinessEnum.EnumVerifyMethod;
import com.cbs.oukasystem.common.BusinessEnum.EnumVerifyType;
import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUVerifyVO extends NormalVO {

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

    @Schema(name = "isPwd", description = "是否是修改密码")
    private Boolean isPwd;
}
