package com.cbs.oukasystem.controller.operate;

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

import com.cbs.oukasystem.service.operate.CallService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.operate.IUCallVO;
import com.cbs.oukasystem.vo.in.operate.QueryCallVO;
import com.cbs.oukasystem.vo.out.operate.CallVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "点呼 CallApi")
@RequestMapping("/api/operate/call")
@CrossOrigin
@RestController
public class CallController {

    @Autowired
    private CallService service;

    @ResponseBody
    @Operation(summary = "CallDetail -点呼情報")
    @GetMapping("/detail/{id}")
    public ResultVO<CallVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All Call - すべての点呼")
    @GetMapping("/all")
    public ResultVO<List<CallVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "Call Page List -点呼のリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<CallVO>> getPages(@RequestBody QueryCallVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit Call - 追加・編集点呼")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "iuCallVO", description = "追加・編集点呼", required = true))
    public ResultVO<CallVO> insertOrUpdate(@Validated @RequestBody IUCallVO iuCallVO) {
        return ResultVO.success(service.addOrEdit(iuCallVO));
    }

    @ResponseBody
    @Operation(summary = "setAmCall Call - 乗務前点呼")
    @PostMapping(value = "/setAmCall")
    @Parameters(@Parameter(name = "iuCallVO", description = "乗務前点呼", required = true))
    public ResultVO<CallVO> setAmCall(@Validated @RequestBody IUCallVO iuCallVO) {
        return ResultVO.success(service.setAmCall(iuCallVO));
    }

    @ResponseBody
    @Operation(summary = "setPmCall Call - 乗務後点呼")
    @PostMapping(value = "/setPmCall")
    @Parameters(@Parameter(name = "iuCallVO", description = "乗務後点呼", required = true))
    public ResultVO<CallVO> setPmCall(@Validated @RequestBody IUCallVO iuCallVO) {
        return ResultVO.success(service.setPmCall(iuCallVO));
    }

    @ResponseBody
    @Operation(summary = "Delete Call - 削除点呼")
    @DeleteMapping("/delete/{id}")
    public ResultVO<Boolean> delete(@PathVariable String id) {
        return ResultVO.success(service.delete(id));
    }

    @ResponseBody
    @Operation(summary = "Delete Call - 物理削除点呼")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

    @ResponseBody
    @Operation(summary = "Audit Call - 審査点呼")
    @PutMapping("/audit/{id}")
    public ResultVO<Boolean> audit(@PathVariable String id) {
        return ResultVO.success(service.audit(id));
    }
}
