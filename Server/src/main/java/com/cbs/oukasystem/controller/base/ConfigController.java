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

import com.cbs.oukasystem.service.base.ConfigService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.base.IUConfigVO;
import com.cbs.oukasystem.vo.in.base.QueryConfigVO;
import com.cbs.oukasystem.vo.out.base.ConfigVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "基礎設定 ConfigApi")
@RequestMapping("/api/base/config")
@CrossOrigin
@RestController
public class ConfigController {

    @Autowired
    private ConfigService service;

    @ResponseBody
    @Operation(summary = "ConfigDetail - 基礎設定情報")
    @GetMapping("/detail/{id}")
    public ResultVO<ConfigVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All Configs - すべての基礎設定 ")
    @GetMapping("/all")
    public ResultVO<List<ConfigVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "Configs Page List - 基礎設定のリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<ConfigVO>> getPages(@RequestBody QueryConfigVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit Config - 追加・編集 基礎設定")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "iuConfigVO", description = "追加・編集 基礎設定", required = true))
    public ResultVO<ConfigVO> insertOrUpdate(@Validated @RequestBody IUConfigVO iuConfigVO) {
        return ResultVO.success(service.addOrEdit(iuConfigVO));
    }

    @ResponseBody
    @Operation(summary = "Delete Config - 物理削除 基礎設定")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

}