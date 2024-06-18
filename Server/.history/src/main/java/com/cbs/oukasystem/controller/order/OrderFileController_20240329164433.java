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

import com.cbs.oukasystem.service.order.OrderFileService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.order.IUOrderFileVO;
import com.cbs.oukasystem.vo.in.order.QueryOrderFileVO;
import com.cbs.oukasystem.vo.out.order.OrderFileVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "ファイル OrderFileApi")
@RequestMapping("/api/order/file")
@CrossOrigin
@RestController
public class OrderFileController {

    @Autowired
    private OrderFileService service;

    @ResponseBody
    @Operation(summary = "OrderFileDetail -ファイル情報")
    @GetMapping("/details/{id}")
    public ResultVO<OrderFileVO> details(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All OrderFile - すべてのファイル")
    @GetMapping("/all")
    public ResultVO<List<OrderFileVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "From OrderFile Page List -ファイルのリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<OrderFileVO>> getPages(@RequestBody QueryOrderFileVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit Pay - 追加・編集ファイル")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "iuVo", description = "追加・編集ファイル", required = true))
    public ResultVO<OrderFileVO> insertOrUpdate(@Validated @RequestBody IUOrderFileVO iuVo) {
        return ResultVO.success(service.addOrEdit(iuVo));
    }

    @ResponseBody
    @Operation(summary = "setShare OrderFile")
    @PutMapping("/setShare/{id}")
    public ResultVO<Boolean> setShare(@PathVariable String id) {
        return ResultVO.success(service.delete(id));
    }

    @ResponseBody
    @Operation(summary = "Delete OrderFile - 削除ファイル")
    @DeleteMapping("/delete/{id}")
    public ResultVO<Boolean> delete(@PathVariable String id) {
        return ResultVO.success(service.delete(id));
    }

    @ResponseBody
    @Operation(summary = "Delete OrderFile - 物理削除ファイル")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

    @ResponseBody
    @Operation(summary = "Audit OrderFile - 審査ファイル")
    @PutMapping("/audit/{id}")
    public ResultVO<Boolean> audit(@PathVariable String id) {
        return ResultVO.success(service.audit(id));
    }
}
