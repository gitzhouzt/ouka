package com.cbs.oukasystem.vo.in.user;

import com.cbs.oukasystem.common.BusinessEnum.EnumVerifyMethod;
import com.cbs.oukasystem.common.BusinessEnum.EnumVerifyType;
import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class RegisterVO extends NormalVO {

    @Schema(name = "loginPass")
    private String loginPass;

    @Schema(name = "userEmail", description = "メールアドレス", example = "example@example.com", required = true)
    private String userEmail;

    @Schema(name = "countryNum", description = "区番号", example = "81")
    private int countryNum;

    @Schema(name = "userPhone", description = "携帯", example = "0801111222")
    private String userPhone;

    @Schema(name = "code")
    private String code = "";

    @Schema(name = "content")
    private String content = "";

    @Schema(name = "verifyType")
    private EnumVerifyType verifyType;

    @Schema(name = "verifyMethod")
    private EnumVerifyMethod verifyMethod;
}
