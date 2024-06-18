package com.cbs.oukasystem.controller.driverApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cbs.oukasystem.service.driverApp.DriverHomeService;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.out.driver.TodayVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;

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

}
