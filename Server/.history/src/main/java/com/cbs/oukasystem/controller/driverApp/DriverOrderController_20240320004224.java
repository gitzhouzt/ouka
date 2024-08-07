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
import com.cbs.oukasystem.vo.out.driverApp.OrderDetailsVO;
import com.cbs.oukasystem.vo.out.driverApp.TodayVO;
import com.cbs.oukasystem.vo.out.finance.PayRecordVO;
import com.cbs.oukasystem.vo.out.order.OrderVO;
import com.cbs.oukasystem.vo.out.user.UserLogVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "ドライバーアプリ DriverApi")
@RequestMapping("/api/driver/order")
@CrossOrigin
@RestController
public class DriverOrderController {

    @Autowired
    private DriverOrderService service;

    @ResponseBody
    @Operation(summary = "OrderDetail -注文情報")
    @GetMapping("/details/{id}")
    public ResultVO<OrderDetailsVO> details(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getDetails(id));
    }

    @ResponseBody
    @Operation(summary = "Order Page List -注文のリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<OrderVO>> getPages(@RequestBody QueryOrderVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "confirm Order - 确认订单")
    @PutMapping(value = "/confirm/{id}")
    public ResultVO<Boolean> confirm(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.confirm(id));
    }

    @ResponseBody
    @Operation(summary = "work Order - 开始订单")
    @PutMapping(value = "/work/{id}")
    public ResultVO<Boolean> work(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.work(id));
    }

    @ResponseBody
    @Operation(summary = "complete Order - 完成订单")
    @PutMapping(value = "/complete/{id}")
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

}
