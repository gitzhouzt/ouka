package com.cbs.oukasystem.controller.finance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cbs.oukasystem.service.finance.PayRecordService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.finance.IUPayRecordVO;
import com.cbs.oukasystem.vo.in.finance.QueryPayRecordVO;
import com.cbs.oukasystem.vo.out.finance.PayRecordVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "料金記録 PayRecordApi")
@RequestMapping("/api/finance/payRecord")
@CrossOrigin
@RestController
public class PayRecordController {

    @Autowired
    private PayRecordService service;

    @ResponseBody
    @Operation(summary = "PayDetail -料金記録情報")
    @GetMapping("/detail/{id}")
    public ResultVO<PayRecordVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All Pay - すべての料金記録")
    @GetMapping("/all")
    public ResultVO<List<PayRecordVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "From Pay Page List -料金記録のリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<PayRecordVO>> Pages(@RequestBody QueryPayRecordVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "From Pay Page List -料金記録のリスト")
    @PostMapping("/cash/list")
    public ResultVO<ListVO<PayRecordVO>> cashPages(@RequestBody QueryPayRecordVO queryVO) {
        String[] payMethods = { "現金" };
        queryVO.setPayMethod(payMethods);
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Export 現金入金記録")
    @PostMapping(value = "/cash/export")
    @Parameters(@Parameter(name = "queryVO", description = "現金入金記録", required = true))
    public void exportCashIn(@Validated @RequestBody QueryPayRecordVO queryVO) {
        service.exportCashIn(queryVO);
    }

    @ResponseBody
    @Operation(summary = "From Pay Page List -出金記録のリスト")
    @PostMapping("/out/list")
    public ResultVO<ListVO<PayRecordVO>> outPages(@RequestBody QueryPayRecordVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Export 出金記録")
    @PostMapping(value = "/out/export")
    @Parameters(@Parameter(name = "queryVO", description = "出金記録", required = true))
    public void exportOut(@Validated @RequestBody QueryPayRecordVO queryVO) {
        service.exportOut(queryVO);
    }

    @ResponseBody
    @Operation(summary = "From Pay Page List -前受金記録のリスト")
    @PostMapping("/mae/list")
    public ResultVO<ListVO<PayRecordVO>> maePages(@RequestBody QueryPayRecordVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Export 前受金記録")
    @PostMapping(value = "/mae/export")
    @Parameters(@Parameter(name = "queryVO", description = "前受金記録", required = true))
    public void exportMae(@Validated @RequestBody QueryPayRecordVO queryVO) {
        service.exportMae(queryVO);
    }

    @ResponseBody
    @Operation(summary = "Add/Edit Pay - 追加・編集料金記録")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "iuPayVO", description = "追加・編集料金記録", required = true))
    public ResultVO<PayRecordVO> insertOrUpdate(@Validated @RequestBody IUPayRecordVO iuPayVO) {
        return ResultVO.success(service.addOrEdit(iuPayVO));
    }

    @ResponseBody
    @Operation(summary = "Delete Pay - 削除料金記録")
    @DeleteMapping("/delete/{id}")
    public ResultVO<Boolean> delete(@PathVariable String id) {
        return ResultVO.success(service.delete(id));
    }

    @ResponseBody
    @Operation(summary = "Delete Pay - 物理削除料金記録")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

}
