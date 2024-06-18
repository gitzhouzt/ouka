package com.cbs.oukasystem.vo.in.common;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema
@NoArgsConstructor
@AllArgsConstructor
public class IUInitAdminVO extends NormalVO {

    /*
     * admin
     */

    @NotBlank(message = "名前を入力してください")
    @Length(min = 1, max = 20, message = "1～20文字")
    @Schema(name = "userName", description = "名前", example = "admin", required = true)
    private String userName;

    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "正しいメールアドレスを入力してください")
    @Schema(name = "userEmail", description = "メールアドレス", example = "huang@cbsdata.co.jp", required = true)
    private String userEmail;

    @Schema(name = "userPhone", description = " 電話番号", example = "09012345678")
    private String userPhone;

    @Schema(name = "userPost", description = "所属部署", example = "情報管理室")
    private String userPost;
}
