package com.cbs.oukasystem.controller.user;

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

import com.cbs.oukasystem.service.user.UserLogService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.user.IUUserLogVO;
import com.cbs.oukasystem.vo.in.user.QueryUserLogVO;
import com.cbs.oukasystem.vo.out.user.UserLogVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "仕事日記 UserLogApi")
@RequestMapping("/api/user/log")
@CrossOrigin
@RestController
public class UserLogController {

    @Autowired
    private UserLogService service;

    @ResponseBody
    @Operation(summary = "UserLogDetail - ユーザー情報")
    @GetMapping("/detail/{id}")
    public ResultVO<UserLogVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All UserLogs - すべてのユーザー ")
    @GetMapping("/all")
    public ResultVO<List<UserLogVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "UserLogs Page List - ユーザーのリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<UserLogVO>> getPages(@RequestBody QueryUserLogVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit UserLog - 追加・編集 ユーザー")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "iuUserLogVO", description = "追加・編集 ユーザー", required = true))
    public ResultVO<UserLogVO> addOrEdit(@Validated @RequestBody IUUserLogVO iuUserLogVO) {
        return ResultVO.success(service.addOrEdit(iuUserLogVO));
    }

    @ResponseBody
    @Operation(summary = "Delete UserLog - 削除 ユーザー")
    @DeleteMapping("/delete/{id}")
    public ResultVO<Boolean> delete(@PathVariable String id) {
        return ResultVO.success(service.delete(id));
    }

    @ResponseBody
    @Operation(summary = "Delete UserLog - 物理削除 ユーザー")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

    @ResponseBody
    @Operation(summary = "Audit UserLog - 審査 ユーザー")
    @PutMapping("/audit/{id}")
    public ResultVO<Boolean> audit(@PathVariable String id) {
        return ResultVO.success(service.audit(id));
    }
}
