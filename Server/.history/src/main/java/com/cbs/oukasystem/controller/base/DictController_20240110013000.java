package com.cbs.oukasystem.controller.base;

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

import com.cbs.oukasystem.service.base.DictService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.base.IUDictVO;
import com.cbs.oukasystem.vo.in.base.QueryDictVO;
import com.cbs.oukasystem.vo.out.base.DictVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "辞典 DictApi")
@RequestMapping("/api/base/dict")
@CrossOrigin
@RestController
public class DictController {

    @Autowired
    private DictService service;

    @ResponseBody
    @Operation(summary = "DictDetail - 辞典情報")
    @GetMapping("/detail/{id}")
    public ResultVO<DictVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All Dicts - すべての辞典 ")
    @GetMapping("/all")
    public ResultVO<List<DictVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "Dicts Page List - 辞典のリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<DictVO>> getPages(@RequestBody QueryDictVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit Dict - 追加・編集 辞典")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "iuDictVO", description = "追加・編集 辞典", required = true))
    public ResultVO<DictVO> insertOrUpdate(@Validated @RequestBody IUDictVO iuDictVO) {
        return ResultVO.success(service.addOrEdit(iuDictVO));
    }

    @ResponseBody
    @Operation(summary = "Delete Dict - 物理削除 辞典")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

}