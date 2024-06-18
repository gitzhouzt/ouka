package com.cbs.oukasystem.controller.driverApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cbs.oukasystem.service.driverApp.DriverUserService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.order.QueryOrderVO;
import com.cbs.oukasystem.vo.out.order.OrderVO;
import com.cbs.oukasystem.vo.out.user.UserDriveVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;

@Api(tags = "ドライバーアプリ DriverApi")
@RequestMapping("/api/driver/user")
@CrossOrigin
@RestController
public class DriverUserController {

    @Autowired
    private DriverUserService service;

    @ResponseBody
    @Operation(summary = "UserDetail -司机情報")
    @GetMapping("/details")
    public ResultVO<UserDriveVO> details() {
        return ResultVO.success(service.getDriverInfo());
    }

    @ResponseBody
    @Operation(summary = "log Page List -注文のリスト")
    @PostMapping("/log/list")
    public ResultVO<ListVO<OrderVO>> getPages(@RequestBody QueryOrderVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }
}
