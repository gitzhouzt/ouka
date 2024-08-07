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

import com.cbs.oukasystem.service.user.UserHolidayService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.user.IUUserHolidayVO;
import com.cbs.oukasystem.vo.in.user.QueryUserHolidayVO;
import com.cbs.oukasystem.vo.in.user.ResetPwdVO;
import com.cbs.oukasystem.vo.out.user.UserHolidayVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "休業 UserHolidayHolidayApi")
@RequestMapping("/api/user/holiday")
@CrossOrigin
@RestController
public class UserHolidayController {

    @Autowired
    private UserHolidayService service;

    @ResponseBody
    @Operation(summary = "UserHolidayDetail - ユーザー情報")
    @GetMapping("/detail/{id}")
    public ResultVO<UserHolidayVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All UserHolidays - すべてのユーザー ")
    @GetMapping("/all")
    public ResultVO<List<UserHolidayVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "UserHolidays Page List - ユーザーのリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<UserHolidayVO>> getPages(@RequestBody QueryUserHolidayVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit UserHoliday - 追加・編集 ユーザー")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "InsertOrUpdateUserHolidayVO", description = "追加・編集 ユーザー", required = true))
    public ResultVO<UserHolidayVO> insertOrUpdate(@Validated @RequestBody IUUserHolidayVO iuUserHolidayVO) {
        return ResultVO.success(service.insertOrUpdate(iuUserHolidayVO));
    }

    @ResponseBody
    @Operation(summary = "Delete UserHoliday - 削除 ユーザー")
    @DeleteMapping("/delete/{id}")
    public ResultVO<Boolean> delete(@PathVariable String id) {
        return ResultVO.success(service.delete(id));
    }

    @ResponseBody
    @Operation(summary = "Delete UserHoliday - 物理削除 ユーザー")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

    @ResponseBody
    @Operation(summary = "Audit UserHoliday - 審査 ユーザー")
    @PutMapping("/audit/{id}")
    public ResultVO<Boolean> audit(@PathVariable String id) {
        return ResultVO.success(service.audit(id));
    }
}
