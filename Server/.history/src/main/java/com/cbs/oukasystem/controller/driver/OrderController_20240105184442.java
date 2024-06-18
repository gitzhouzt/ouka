package com.cbs.oukasystem.controller.driver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cbs.oukasystem.service.order.OrderService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.order.IUOrderVO;
import com.cbs.oukasystem.vo.in.order.IUPayRecordVO;
import com.cbs.oukasystem.vo.in.order.OrderCancelVO;
import com.cbs.oukasystem.vo.in.order.QueryOrderVO;
import com.cbs.oukasystem.vo.in.order.OrderDeployVO;
import com.cbs.oukasystem.vo.out.order.DeployDetailsVO;
import com.cbs.oukasystem.vo.out.order.OrderVO;
import com.cbs.oukasystem.vo.out.order.PayDetailsVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "ドライバーアプリ DriverApi")
@RequestMapping("/api/driver/order")
@CrossOrigin
@RestController
public class OrderController {

    @Autowired
    private OrderService service;

    @ResponseBody
    @Operation(summary = "OrderDetail -注文情報")
    @GetMapping("/details/{id}")
    public ResultVO<OrderVO> details(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All Order - すべての注文")
    @GetMapping("/all")
    public ResultVO<List<OrderVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "From Order Page List -注文のリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<OrderVO>> getPages(@RequestBody QueryOrderVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }
}
