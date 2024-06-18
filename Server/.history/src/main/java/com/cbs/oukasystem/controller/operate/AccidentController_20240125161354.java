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

import com.cbs.oukasystem.service.operate.AccidentService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.operate.IUAccidentVO;
import com.cbs.oukasystem.vo.in.operate.QueryAccidentVO;
import com.cbs.oukasystem.vo.out.operate.AccidentVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "事故 AccidentApi")
@RequestMapping("/api/operate/accident")
@CrossOrigin
@RestController
public class AccidentController {

    @Autowired
    private AccidentService service;

    @ResponseBody
    @Operation(summary = "AccidentDetail -事故情報")
    @GetMapping("/detail/{id}")
    public ResultVO<AccidentVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All Accident - すべての事故")
    @GetMapping("/all")
    public ResultVO<List<AccidentVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "Accident Page List -事故のリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<AccidentVO>> getPages(@RequestBody QueryAccidentVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit Accident - 追加・編集事故")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "iuAccidentVO", description = "追加・編集事故", required = true))
    public ResultVO<AccidentVO> insertOrUpdate(@Validated @RequestBody IUAccidentVO iuAccidentVO) {
        return ResultVO.success(service.addOrEdit(iuAccidentVO));
    }

    @ResponseBody
    @Operation(summary = "setAccident Car - 新規事故")
    @PostMapping(value = "/setAccident")
    @Parameters(@Parameter(name = "iuAccidentVO", description = "新規事故", required = true))
    public ResultVO<AccidentVO> setAccident(@Validated @RequestBody IUAccidentVO iuAccidentVO) {
        return ResultVO.success(service.setAccident(iuAccidentVO));
    }

    @ResponseBody
    @Operation(summary = "setAmount Car - 設定金額")
    @PostMapping(value = "/setAmount")
    @Parameters(@Parameter(name = "iuAccidentVO", description = "設定事故金額", required = true))
    public ResultVO<AccidentVO> setAmount(@Validated @RequestBody IUAccidentVO iuAccidentVO) {
        return ResultVO.success(service.setAmount(iuAccidentVO));
    }

    @ResponseBody
    @Operation(summary = "Delete Accident - 削除事故")
    @DeleteMapping("/delete/{id}")
    public ResultVO<Boolean> delete(@PathVariable String id) {
        return ResultVO.success(service.delete(id));
    }

    @ResponseBody
    @Operation(summary = "Delete Accident - 物理削除事故")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

    @ResponseBody
    @Operation(summary = "Audit Accident - 審査事故")
    @PutMapping("/audit/{id}")
    public ResultVO<Boolean> audit(@PathVariable String id) {
        return ResultVO.success(service.audit(id));
    }
}
