package com.cbs.oukasystem.controller.finance;

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

import com.cbs.oukasystem.service.finance.EarningsService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.finance.IUAdvanceVO;
import com.cbs.oukasystem.vo.in.finance.QueryEarningsVO;
import com.cbs.oukasystem.vo.out.finance.AdvanceVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "入金 AdvanceApi")
@RequestMapping("/api/finance/advance")
@CrossOrigin
@RestController
public class AdvanceController {

    @Autowired
    private EarningsService service;

    @ResponseBody
    @Operation(summary = "AdvanceDetail -入金情報")
    @GetMapping("/detail/{id}")
    public ResultVO<AdvanceVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All Advance - すべての入金")
    @GetMapping("/all")
    public ResultVO<List<AdvanceVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "Advance Page List -入金のリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<AdvanceVO>> getPages(@RequestBody QueryEarningsVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit Advance - 追加・編集入金")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "iuAdvanceVO", description = "追加・編集入金", required = true))
    public ResultVO<AdvanceVO> insertOrUpdate(@Validated @RequestBody IUAdvanceVO iuAdvanceVO) {
        return ResultVO.success(service.addOrEdit(iuAdvanceVO));
    }

    @ResponseBody
    @Operation(summary = "Delete Advance - 削除入金")
    @DeleteMapping("/delete/{id}")
    public ResultVO<Boolean> delete(@PathVariable String id) {
        return ResultVO.success(service.delete(id));
    }

    @ResponseBody
    @Operation(summary = "Delete Advance - 物理削除入金")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

    @ResponseBody
    @Operation(summary = "Audit Advance - 審査入金")
    @PutMapping("/audit/{id}")
    public ResultVO<Boolean> audit(@PathVariable String id) {
        return ResultVO.success(service.audit(id));
    }
}
