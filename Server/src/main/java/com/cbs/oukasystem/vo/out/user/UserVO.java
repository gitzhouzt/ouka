package com.cbs.oukasystem.vo.out.user;

import com.cbs.oukasystem.common.CommonEnum.EnumSex;
import com.cbs.oukasystem.common.CommonEnum.EnumUserStatus;
import com.cbs.oukasystem.vo.OperatorVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(name = "UserVO")
@NoArgsConstructor
@AllArgsConstructor
public class UserVO extends OperatorVO {

    @Schema(name = "loginName", description = "アカウント", example = "example")
    private String loginName;

    @Schema(name = "loginPass", description = "パスワード", example = "123456")
    private String loginPass;

    @Schema(name = "userNo", description = "番号", example = "example")
    private String userNo;

    @Schema(name = "userName", description = "名前・ワード", example = "example")
    private String userName;

    @Schema(name = "realName")
    private String realName;

    @Schema(name = "userEmail", description = "メール", example = "example@example.com")
    private String userEmail;

    @Schema(name = "countryNum", description = "区番号", example = "81")
    private int countryNum = 81;

    @Schema(name = "userPhone", description = "携帯", example = "0801111222")
    private String userPhone;

    @Schema(name = "userAvatar", description = "アバター")
    private String userAvatar;

    @Schema(name = "userAddress")
    private String userAddress;

    @Schema(name = "userSex", description = " 性別")
    private EnumSex userSex;

    @Schema(name = "userSexName", description = " 性別")
    private String userSexName;

    @Schema(name = "userPost")
    private String userPost;

    @Schema(name = "userWechat")
    private String userWechat;

    @Schema(name = "userLine")
    private String userLine;

    @Schema(name = "userWhatsApp")
    private String userWhatsApp;

    @Schema(name = "userRoles", description = "役職", example = "STAFF")
    private String userRoles;

    @Schema(name = "userRolesName", description = "役職", example = "STAFF")
    private String userRolesName;

    @Schema(name = "status")
    private EnumUserStatus status = EnumUserStatus.Working;

    @Schema(name = "statusName")
    private String statusName = EnumUserStatus.Working.getMessage();

    @Schema(name = "remark", description = "備考")
    private String remark;

}
