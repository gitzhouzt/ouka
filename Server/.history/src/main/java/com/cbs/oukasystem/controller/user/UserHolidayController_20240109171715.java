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

import com.cbs.oukasystem.service.user.UserRestService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.user.IUUserRestVO;
import com.cbs.oukasystem.vo.in.user.QueryUserRestVO;
import com.cbs.oukasystem.vo.in.user.ResetPwdVO;
import com.cbs.oukasystem.vo.out.user.UserRestVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "休業 UserRestHolidayApi")
@RequestMapping("/api/user/holiday")
@CrossOrigin
@RestController
public class UserRestController {

    @Autowired
    private UserRestService service;

    @ResponseBody
    @Operation(summary = "UserRestDetail - ユーザー情報")
    @GetMapping("/detail/{id}")
    public ResultVO<UserRestVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All UserRests - すべてのユーザー ")
    @GetMapping("/all")
    public ResultVO<List<UserRestVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "UserRests Page List - ユーザーのリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<UserRestVO>> getPages(@RequestBody QueryUserRestVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit UserRest - 追加・編集 ユーザー")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "InsertOrUpdateUserRestVO", description = "追加・編集 ユーザー", required = true))
    public ResultVO<UserRestVO> insertOrUpdate(@Validated @RequestBody IUUserRestVO iuUserRestVO) {
        return ResultVO.success(service.insertOrUpdate(iuUserRestVO));
    }

    @ResponseBody
    @Operation(summary = "Delete UserRest - 削除 ユーザー")
    @DeleteMapping("/delete/{id}")
    public ResultVO<Boolean> delete(@PathVariable String id) {
        return ResultVO.success(service.delete(id));
    }

    @ResponseBody
    @Operation(summary = "Delete UserRest - 物理削除 ユーザー")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

    @ResponseBody
    @Operation(summary = "Audit UserRest - 審査 ユーザー")
    @PutMapping("/audit/{id}")
    public ResultVO<Boolean> audit(@PathVariable String id) {
        return ResultVO.success(service.audit(id));
    }
}
