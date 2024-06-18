package com.cbs.oukasystem.vo.out.login;

import java.io.Serializable;

import com.cbs.oukasystem.vo.out.user.UserVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(name = "LoginUserVO")
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserVO implements Serializable {

    @Schema
    private UserVO userVO;

    @Schema
    private TokenVO tokenVO;
}
