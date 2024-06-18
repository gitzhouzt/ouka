package com.cbs.oukasystem.controller.driverApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cbs.oukasystem.service.driverApp.DriverHomeService;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.operate.IUCallVO;
import com.cbs.oukasystem.vo.out.driverApp.TodayVO;
import com.cbs.oukasystem.vo.out.operate.CallVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "ドライバーアプリ DriverApi")
@RequestMapping("/api/driver")
@CrossOrigin
@RestController
public class DriverHomeController {

    @Autowired
    private DriverHomeService service;

    @ResponseBody
    @Operation(summary = "todayOrders - 今日订单情報")
    @GetMapping("/todayOrders")
    public ResultVO<TodayVO> todayOrders() {
        return ResultVO.success(service.getTodayOrders());
    }

    @ResponseBody
    @Operation(summary = "setAmCall Call - 乗務前点呼")
    @PostMapping(value = "/setAmCall")
    @Parameters(@Parameter(name = "iuCallVO", description = "乗務前点呼", required = true))
    public ResultVO<CallVO> setAmCall(@Validated @RequestBody IUCallVO iuCallVO) {
        return ResultVO.success(service.setAmCall(iuCallVO));
    }

    @ResponseBody
    @Operation(summary = "setPmCall Call - 乗務後点呼")
    @PostMapping(value = "/setPmCall")
    @Parameters(@Parameter(name = "iuCallVO", description = "乗務後点呼", required = true))
    public ResultVO<CallVO> setPmCall(@Validated @RequestBody IUCallVO iuCallVO) {
        return ResultVO.success(service.setPmCall(iuCallVO));
    }

}
