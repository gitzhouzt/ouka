package com.cbs.oukasystem.controller.order.driver;

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

import com.cbs.oukasystem.service.order.driver.DriverOrderService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.order.IUPayRecordVO;
import com.cbs.oukasystem.vo.in.order.QueryOrderVO;
import com.cbs.oukasystem.vo.in.user.QueryUserLogVO;
import com.cbs.oukasystem.vo.out.car.CarVO;
import com.cbs.oukasystem.vo.out.driver.OrderDetailsVO;
import com.cbs.oukasystem.vo.out.driver.TodayVO;
import com.cbs.oukasystem.vo.out.order.OrderVO;
import com.cbs.oukasystem.vo.out.order.PayRecordVO;
import com.cbs.oukasystem.vo.out.user.UserLogVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "ドライバーアプリ DriverApi")
@RequestMapping("/api/driver")
@CrossOrigin
@RestController
public class DriverController {

    @Autowired
    private DriverOrderService service;

    @ResponseBody
    @Operation(summary = "OrderDetail -注文情報")
    @GetMapping("/order/details/{id}")
    public ResultVO<OrderDetailsVO> details(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getDetails(id));
    }

    @ResponseBody
    @Operation(summary = "Order Page List -注文のリスト")
    @PostMapping("/order/list")
    public ResultVO<ListVO<OrderVO>> getPages(@RequestBody QueryOrderVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "work Order - 开始订单")
    @PutMapping(value = "/order/work/{id}")
    public ResultVO<Boolean> work(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.work(id));
    }

    @ResponseBody
    @Operation(summary = "complete Order - 完成订单")
    @PutMapping(value = "/order/complete/{id}")
    public ResultVO<Boolean> complete(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.complete(id));
    }

    // @ResponseBody
    // @Operation(summary = "apply Order - 费用申请")
    // @PostMapping(value = "/pay/apply")
    // @Parameters(@Parameter(name = "iuPayVO", description = "支付记录", required =
    // true))
    // public ResultVO<Boolean> apply(@Validated @RequestBody IUPayRecordVO iuPayVO)
    // {
    // return ResultVO.success(service.apply(iuPayVO));
    // }

    @ResponseBody
    @Operation(summary = "Car List")
    @PostMapping("/car/list")
    public ResultVO<ListVO<CarVO>> getTodayCars() {
        return ResultVO.success(service.getTodayCars());
    }

    @ResponseBody
    @Operation(summary = "Log Page List -注文のリスト")
    @PostMapping("/log/list")
    public ResultVO<ListVO<UserLogVO>> getLogs() {
        return ResultVO.success(service.getLogs());
    }

    @ResponseBody
    @Operation(summary = "todayOrders - 今日订单情報")
    @GetMapping("/todayOrders")
    public ResultVO<TodayVO> todayOrders() {
        return ResultVO.success(service.getTodayOrders());
    }

}
