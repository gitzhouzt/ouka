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

import com.cbs.oukasystem.service.order.PayItemService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.order.IUPayItemVO;
import com.cbs.oukasystem.vo.in.order.QueryOrderVO;
import com.cbs.oukasystem.vo.in.order.QueryPayItemVO;
import com.cbs.oukasystem.vo.out.order.PayItemVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "現金入金項目 PayItemApi")
@RequestMapping("/api/order/payItem")
@CrossOrigin
@RestController
public class PayItemController {

    @Autowired
    private PayItemService service;

    @ResponseBody
    @Operation(summary = "PayDetail -現金入金項目情報")
    @GetMapping("/detail/{id}")
    public ResultVO<PayItemVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All Pay - すべての現金入金項目")
    @GetMapping("/all")
    public ResultVO<List<PayItemVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "From Pay Page List -現金入金項目のリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<PayItemVO>> getPages(@RequestBody QueryPayItemVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit Pay - 追加・編集現金入金項目")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "iuPayVO", description = "追加・編集現金入金項目", required = true))
    public ResultVO<PayItemVO> insertOrUpdate(@Validated @RequestBody IUPayItemVO iuPayVO) {
        return ResultVO.success(service.addOrEdit(iuPayVO));
    }

    @ResponseBody
    @Operation(summary = "Delete Pay - 削除現金入金項目")
    @DeleteMapping("/delete/{id}")
    public ResultVO<Boolean> delete(@PathVariable String id) {
        return ResultVO.success(service.delete(id));
    }

    @ResponseBody
    @Operation(summary = "Delete Pay - 物理削除現金入金項目")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

    @ResponseBody
    @Operation(summary = "Audit Pay - 審査現金入金項目")
    @PutMapping("/audit/{id}")
    public ResultVO<Boolean> audit(@PathVariable String id) {
        return ResultVO.success(service.audit(id));
    }
}