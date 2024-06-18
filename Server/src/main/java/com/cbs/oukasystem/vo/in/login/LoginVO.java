package com.cbs.oukasystem.vo.in.login;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class LoginVO implements Serializable {

    @NotBlank(message = "ログインアカウントを入力してください")
    @Schema(name = "loginName", description = "アカウント", example = "loginName", required = true)
    private String loginName;

    @NotBlank(message = "パスワードを入力してください")
    @Schema(name = "loginPass", description = "パスワード", example = "loginPass", required = true)
    private String loginPass;

    @Schema(name = "isAdmin")
    private Boolean isAdmin;

}
