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

import com.cbs.oukasystem.service.user.UserDriveService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.operate.QueryScheduleVO;
import com.cbs.oukasystem.vo.in.user.IUUserDriveVO;
import com.cbs.oukasystem.vo.in.user.QueryUserDriveVO;
import com.cbs.oukasystem.vo.in.user.ResetPwdVO;
import com.cbs.oukasystem.vo.out.operate.ScheduleVO;
import com.cbs.oukasystem.vo.out.user.UserDriveVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "ドライブ UserDriveApi")
@RequestMapping("/api/user")
@CrossOrigin
@RestController
public class UserDriveController {

    @Autowired
    private UserDriveService service;

    @ResponseBody
    @Operation(summary = "UserDriveDetail - ドライブ情報")
    @GetMapping("/detail/{id}")
    public ResultVO<UserDriveVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All UserDrives - すべてのドライブ ")
    @GetMapping("/all")
    public ResultVO<List<UserDriveVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "UserDrives Page List - ドライブのリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<UserDriveVO>> getPages(@RequestBody QueryUserDriveVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Call Driver Page List - ドライバーのリスト")
    @PostMapping("/callDriver/list")
    public ResultVO<ListVO<UserDriveVO>> getPagesByCallDriver(@RequestBody QueryUserDriveVO queryVO) {
        return ResultVO.success(service.getPagesByCallDriver(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit UserDrive - 追加・編集 ドライブ")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "InsertOrUpdateUserDriveVO", description = "追加・編集 ドライブ", required = true))
    public ResultVO<UserDriveVO> insertOrUpdate(@Validated @RequestBody IUUserDriveVO iuUserDriveVO) {
        return ResultVO.success(service.insertOrUpdate(iuUserDriveVO));
    }

    @ResponseBody
    @Operation(summary = "ResetPwd UserDrive - パスワード変更")
    @PostMapping(value = "/resetPwd")
    @Parameters(@Parameter(name = "restPwdVO", description = "追加・編集 ドライブ", required = true))
    public ResultVO<Boolean> resetPwd(@Validated @RequestBody ResetPwdVO restPwdVO) {
        return ResultVO.success(service.resetPwd(restPwdVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit UserDriveRest - 追加・編集 休暇")
    @PostMapping(value = "/rest")
    @Parameters(@Parameter(name = "iuUserDriveRestVO", description = "追加・編集 休暇", required = true))
    public ResultVO<Boolean> rest(@Validated @RequestBody IUUserDriveRestVO iuUserDriveRestVO) {
        return ResultVO.success(service.rest(iuUserDriveRestVO));
    }

    @ResponseBody
    @Operation(summary = "Delete UserDrive - 削除 ドライブ")
    @DeleteMapping("/delete/{id}")
    public ResultVO<Boolean> delete(@PathVariable String id) {
        return ResultVO.success(service.delete(id));
    }

    @ResponseBody
    @Operation(summary = "Delete UserDrive - 物理削除 ドライブ")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

    @ResponseBody
    @Operation(summary = "Audit UserDrive - 審査 ドライブ")
    @PutMapping("/audit/{id}")
    public ResultVO<Boolean> audit(@PathVariable String id) {
        return ResultVO.success(service.audit(id));
    }

    @ResponseBody
    @Operation(summary = "schedule -スケジュールのリスト")
    @PostMapping("/schedule/list")
    public ResultVO<ListVO<ScheduleVO>> schedule(@RequestBody QueryScheduleVO queryVO) {
        return ResultVO.success(service.getPagesBySchedule(queryVO));
    }
}
