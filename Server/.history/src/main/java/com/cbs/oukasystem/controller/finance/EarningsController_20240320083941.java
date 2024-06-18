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
import com.cbs.oukasystem.vo.in.finance.IUEarningsVO;
import com.cbs.oukasystem.vo.in.finance.QueryEarningsVO;
import com.cbs.oukasystem.vo.out.finance.EarningsVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "营业额 EarningsApi")
@RequestMapping("/api/finance/earnings")
@CrossOrigin
@RestController
public class EarningsController {

    @Autowired
    private EarningsService service;

    @ResponseBody
    @Operation(summary = "EarningsDetail -立替情報")
    @GetMapping("/detail/{id}")
    public ResultVO<EarningsVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All Earnings - すべての立替")
    @GetMapping("/all")
    public ResultVO<List<EarningsVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "Earnings Page List -立替のリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<EarningsVO>> getPages(@RequestBody QueryEarningsVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit Earnings - 追加・編集立替")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "iuEarningsVO", description = "追加・編集立替", required = true))
    public ResultVO<EarningsVO> insertOrUpdate(@Validated @RequestBody IUEarningsVO iuEarningsVO) {
        return ResultVO.success(service.addOrEdit(iuEarningsVO));
    }

    @ResponseBody
    @Operation(summary = "Delete Earnings - 削除立替")
    @DeleteMapping("/delete/{id}")
    public ResultVO<Boolean> delete(@PathVariable String id) {
        return ResultVO.success(service.delete(id));
    }

    @ResponseBody
    @Operation(summary = "Delete Earnings - 物理削除立替")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

    @ResponseBody
    @Operation(summary = "Audit Earnings - 審査立替")
    @PutMapping("/audit/{id}")
    public ResultVO<Boolean> audit(@PathVariable String id) {
        return ResultVO.success(service.audit(id));
    }
}
