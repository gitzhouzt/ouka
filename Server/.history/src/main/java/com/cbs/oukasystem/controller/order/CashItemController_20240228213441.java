package com.cbs.oukasystem.controller.order;

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

import com.cbs.oukasystem.service.order.CashItemService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.order.IUCashItemVO;
import com.cbs.oukasystem.vo.in.order.QueryOrderVO;
import com.cbs.oukasystem.vo.in.order.QueryCashItemVO;
import com.cbs.oukasystem.vo.out.order.CarItemVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "現金入金項目 CashItemApi")
@RequestMapping("/api/order/payItem")
@CrossOrigin
@RestController
public class CashItemController {

    @Autowired
    private CashItemService service;

    @ResponseBody
    @Operation(summary = "CashDetail -現金入金項目情報")
    @GetMapping("/detail/{id}")
    public ResultVO<CarItemVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All Cash - すべての現金入金項目")
    @GetMapping("/all")
    public ResultVO<List<CarItemVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "From Cash Page List -現金入金項目のリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<CarItemVO>> getPages(@RequestBody QueryCashItemVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit Cash - 追加・編集現金入金項目")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "iuCashVO", description = "追加・編集現金入金項目", required = true))
    public ResultVO<CarItemVO> insertOrUpdate(@Validated @RequestBody IUCashItemVO iuCashVO) {
        return ResultVO.success(service.addOrEdit(iuCashVO));
    }

    @ResponseBody
    @Operation(summary = "Delete Cash - 削除現金入金項目")
    @DeleteMapping("/delete/{id}")
    public ResultVO<Boolean> delete(@PathVariable String id) {
        return ResultVO.success(service.delete(id));
    }

    @ResponseBody
    @Operation(summary = "Delete Cash - 物理削除現金入金項目")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

    @ResponseBody
    @Operation(summary = "Audit Cash - 審査現金入金項目")
    @PutMapping("/audit/{id}")
    public ResultVO<Boolean> audit(@PathVariable String id) {
        return ResultVO.success(service.audit(id));
    }
}
