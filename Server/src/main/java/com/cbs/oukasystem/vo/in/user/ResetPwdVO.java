package com.cbs.oukasystem.vo.in.user;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.cbs.oukasystem.common.BusinessEnum.EnumVerifyMethod;
import com.cbs.oukasystem.common.BusinessEnum.EnumVerifyType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class ResetPwdVO implements Serializable {

    @Schema(name = "userId", description = "userId", required = false)
    private String userId;

    @Schema(name = "userEmail", description = "メールアドレス", example = "example@example.com", required = true)
    private String userEmail;

    @Schema(name = "code")
    private String code = "";

    @Schema(name = "content")
    private String content = "";

    @Schema(name = "verifyType")
    private EnumVerifyType verifyType;

    @Schema(name = "verifyMethod")
    private EnumVerifyMethod verifyMethod;

    @Schema(name = "pwd", description = "現在パスワード", required = false)
    private String pwd;

    @NotBlank(message = "新しいパスワードを入力してください")
    @Schema(name = "newPwd", description = "新しいパスワード", required = true)
    private String newPwd;

    @NotBlank(message = "確認パスワードを入力してください")
    @Schema(name = "confirmPwd", description = "確認パスワード", required = true)
    private String confirmPwd;

}
