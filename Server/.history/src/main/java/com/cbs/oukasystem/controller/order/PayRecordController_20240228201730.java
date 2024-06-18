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

import com.cbs.oukasystem.service.order.PayRecordService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.order.IUPayRecordVO;
import com.cbs.oukasystem.vo.in.order.QueryPayItemVO;
import com.cbs.oukasystem.vo.in.order.QueryPayRecordVO;
import com.cbs.oukasystem.vo.out.order.PayRecordVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "支払い変更 PayRecordApi")
@RequestMapping("/api/order/payRecord")
@CrossOrigin
@RestController
public class PayRecordController {

    @Autowired
    private PayRecordService service;

    @ResponseBody
    @Operation(summary = "PayDetail -支払い変更情報")
    @GetMapping("/detail/{id}")
    public ResultVO<PayRecordVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All Pay - すべての支払い変更")
    @GetMapping("/all")
    public ResultVO<List<PayRecordVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "From Pay Page List -支払い変更のリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<PayRecordVO>> getPages(@RequestBody QueryPayRecordVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Export 注文")
    @PostMapping(value = "/export")
    @Parameters(@Parameter(name = "queryVO", description = "注文", required = true))
    public void export(@Validated @RequestBody QueryPayRecordVO queryVO) {
        service.export(queryVO);
    }

    @ResponseBody
    @Operation(summary = "Add/Edit Pay - 追加・編集支払い変更")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "iuPayVO", description = "追加・編集支払い変更", required = true))
    public ResultVO<PayRecordVO> insertOrUpdate(@Validated @RequestBody IUPayRecordVO iuPayVO) {
        return ResultVO.success(service.addOrEdit(iuPayVO));
    }

    @ResponseBody
    @Operation(summary = "Delete Pay - 削除支払い変更")
    @DeleteMapping("/delete/{id}")
    public ResultVO<Boolean> delete(@PathVariable String id) {
        return ResultVO.success(service.delete(id));
    }

    @ResponseBody
    @Operation(summary = "Delete Pay - 物理削除支払い変更")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

}
