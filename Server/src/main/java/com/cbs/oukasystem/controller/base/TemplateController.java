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

import com.cbs.oukasystem.service.base.TemplateService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.base.IUTemplateVO;
import com.cbs.oukasystem.vo.in.base.QueryTemplateVO;
import com.cbs.oukasystem.vo.out.base.TemplateVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "レンプレート TemplateApi")
@RequestMapping("/api/base/template")
@CrossOrigin
@RestController
public class TemplateController {

    @Autowired
    private TemplateService service;

    @ResponseBody
    @Operation(summary = "TemplateDetail - レンプレート情報")
    @GetMapping("/detail/{id}")
    public ResultVO<TemplateVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All Templates - すべてのレンプレート ")
    @GetMapping("/all")
    public ResultVO<List<TemplateVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "Templates Page List - レンプレートのリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<TemplateVO>> getPages(@RequestBody QueryTemplateVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit Template - 追加・編集 レンプレート")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "iuTemplateVO", description = "追加・編集 レンプレート", required = true))
    public ResultVO<TemplateVO> insertOrUpdate(@Validated @RequestBody IUTemplateVO iuTemplateVO) {
        return ResultVO.success(service.addOrEdit(iuTemplateVO));
    }

    @ResponseBody
    @Operation(summary = "Delete Template - 物理削除 レンプレート")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

}