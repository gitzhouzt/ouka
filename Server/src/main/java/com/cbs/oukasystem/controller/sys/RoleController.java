package com.cbs.oukasystem.controller.sys;

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

import com.cbs.oukasystem.service.sys.RoleService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.sys.IURoleVO;
import com.cbs.oukasystem.vo.in.sys.QueryRoleVO;
import com.cbs.oukasystem.vo.out.sys.RoleVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "役職 RoleApi")
@RequestMapping("/api/sys/role")
@CrossOrigin
@RestController
public class RoleController {

    @Autowired
    private RoleService service;

    @ResponseBody
    @Operation(summary = "RoleDetail - 役職情報")
    @GetMapping("/detail/{id}")
    public ResultVO<RoleVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All Roles - すべての役職 ")
    @GetMapping("/all")
    public ResultVO<List<RoleVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "Areas Page List - 役職のリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<RoleVO>> getPages(@RequestBody QueryRoleVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit Role - 追加・編集 役職")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "InsertOrUpdateUserVO", description = "追加・編集 役職", required = true))
    public ResultVO<RoleVO> insertOrUpdate(@Validated @RequestBody IURoleVO insertOrUpdateVO) {
        return ResultVO.success(service.addOrEdit(insertOrUpdateVO));
    }

    @ResponseBody
    @Operation(summary = "deletePhysics Role - 削除 役職")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

}
