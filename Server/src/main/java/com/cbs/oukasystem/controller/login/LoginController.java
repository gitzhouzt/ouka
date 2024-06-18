package com.cbs.oukasystem.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cbs.oukasystem.common.LoginUtils;
import com.cbs.oukasystem.service.login.LoginService;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.login.LoginVO;
import com.cbs.oukasystem.vo.out.login.LoginUserVO;
import com.cbs.oukasystem.vo.out.login.TokenVO;

import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Api(tags = "ログイン Login")
@RequestMapping("/api")
@CrossOrigin
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @ResponseBody
    @Operation(summary = "Login - ログイン")
    @PostMapping(value = "/login")
    public ResultVO<LoginUserVO> login(@RequestBody @Validated LoginVO loginVO) {
        loginVO.setIsAdmin(true);
        return ResultVO.success(loginService.login(loginVO));
    }

    @ResponseBody
    @Operation(summary = "LogOut - ログアウト")
    @PostMapping(value = "/logout")
    public ResultVO<Boolean> logout() {
        return ResultVO.success(LoginUtils.LOGIN_LOGOUT_CODE, "ok", loginService.logout());
    }

    @ResponseBody
    @Operation(summary = "updateToken - トークンを更新")
    @GetMapping(value = "/updateToken/{tokenRefresh}")
    public ResultVO<TokenVO> updateToken(
            @PathVariable @Parameter(required = true, description = "tokenRefresh") String tokenRefresh) {
        return ResultVO.success(loginService.updateToken(tokenRefresh));
    }

}
