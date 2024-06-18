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

import com.cbs.oukasystem.service.order.OrderGoodsService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.order.IUOrderGoodsVO;
import com.cbs.oukasystem.vo.in.order.QueryOrderGoodsVO;
import com.cbs.oukasystem.vo.out.order.OrderGoodsVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "備品 OrderGoodsApi")
@RequestMapping("/api/order/file")
@CrossOrigin
@RestController
public class OrderGoodsController {

    @Autowired
    private OrderGoodsService service;

    @ResponseBody
    @Operation(summary = "OrderGoodsDetail -備品情報")
    @GetMapping("/details/{id}")
    public ResultVO<OrderGoodsVO> details(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All OrderGoods - すべての備品")
    @GetMapping("/all")
    public ResultVO<List<OrderGoodsVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "From OrderGoods Page List -備品のリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<OrderGoodsVO>> getPages(@RequestBody QueryOrderGoodsVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit Pay - 追加・編集備品")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "iuVo", description = "追加・編集備品", required = true))
    public ResultVO<OrderGoodsVO> insertOrUpdate(@Validated @RequestBody IUOrderGoodsVO iuVo) {
        return ResultVO.success(service.addOrEdit(iuVo));
    }

    @ResponseBody
    @Operation(summary = "Delete OrderGoods - 削除備品")
    @DeleteMapping("/delete/{id}")
    public ResultVO<Boolean> delete(@PathVariable String id) {
        return ResultVO.success(service.delete(id));
    }

    @ResponseBody
    @Operation(summary = "Delete OrderGoods - 物理削除備品")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

    @ResponseBody
    @Operation(summary = "Audit OrderGoods - 審査備品")
    @PutMapping("/audit/{id}")
    public ResultVO<Boolean> audit(@PathVariable String id) {
        return ResultVO.success(service.audit(id));
    }
}
