package com.cbs.oukasystem.controller.driverApp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cbs.oukasystem.service.driverApp.DriverOrderService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.finance.IUPayRecordVO;
import com.cbs.oukasystem.vo.in.order.QueryOrderVO;
import com.cbs.oukasystem.vo.in.user.QueryUserLogVO;
import com.cbs.oukasystem.vo.out.car.CarVO;
import com.cbs.oukasystem.vo.out.driver.OrderDetailsVO;
import com.cbs.oukasystem.vo.out.driver.TodayVO;
import com.cbs.oukasystem.vo.out.finance.PayRecordVO;
import com.cbs.oukasystem.vo.out.order.OrderVO;
import com.cbs.oukasystem.vo.out.user.UserLogVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "ドライバーアプリ DriverApi")
@RequestMapping("/api/driver/user")
@CrossOrigin
@RestController
public class DriverUserController {

    @Autowired
    private DriverOrderService service;

    @ResponseBody
    @Operation(summary = "UserDetail -注文情報")
    @GetMapping("/details/{id}")
    public ResultVO<OrderDetailsVO> details(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getDetails(id));
    }

}
