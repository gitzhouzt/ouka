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

import com.cbs.oukasystem.service.base.AreaService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.base.IUAreaVO;
import com.cbs.oukasystem.vo.in.base.QueryAreaVO;
import com.cbs.oukasystem.vo.out.base.AreaVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "地域 AreaApi")
@RequestMapping("/api/base/area")
@CrossOrigin
@RestController
public class AreaController {

    @Autowired
    private AreaService service;

    @ResponseBody
    @Operation(summary = "AreaDetail - 地域情報")
    @GetMapping("/detail/{id}")
    public ResultVO<AreaVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All Areas - すべての地域 ")
    @GetMapping("/all")
    public ResultVO<List<AreaVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "Areas Page List - 地域のリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<AreaVO>> getPages(@RequestBody QueryAreaVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Areas List - 地域のリスト")
    @PostMapping("/business/list")
    public ResultVO<ListVO<AreaVO>> queryAllArea(@RequestBody QueryAreaVO qVo) {
        return ResultVO.success(service.queryAllArea(qVo));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit Area - 追加・編集 地域")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "iuAreaVO", description = "追加・編集 地域", required = true))
    public ResultVO<AreaVO> insertOrUpdate(@Validated @RequestBody IUAreaVO iuAreaVO) {
        return ResultVO.success(service.addOrEdit(iuAreaVO));
    }

    @ResponseBody
    @Operation(summary = "Delete Area - 物理削除 地域")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

}