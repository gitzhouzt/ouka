package com.cbs.oukasystem.controller.driverApp;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cbs.oukasystem.common.FileUploadUtil;
import com.cbs.oukasystem.common.MessageEnum.EnumUploadCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.service.user.UserService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.operate.QueryScheduleVO;
import com.cbs.oukasystem.vo.in.user.IUUserRestVO;
import com.cbs.oukasystem.vo.in.user.IUUserVO;
import com.cbs.oukasystem.vo.in.user.QueryUserDriveVO;
import com.cbs.oukasystem.vo.in.user.QueryUserVO;
import com.cbs.oukasystem.vo.in.user.ResetPwdVO;
import com.cbs.oukasystem.vo.out.operate.ScheduleVO;
import com.cbs.oukasystem.vo.out.user.UserVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "ユーザー UserApi")
@RequestMapping("/api/user")
@CrossOrigin
@RestController
public class DriverUserController {

    @Autowired
    private UserService service;

    @ResponseBody
    @Operation(summary = "UserDetail - ユーザー情報")
    @GetMapping("/detail/{id}")
    public ResultVO<UserVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All Users - すべてのユーザー ")
    @GetMapping("/all")
    public ResultVO<List<UserVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "Users Page List - ユーザーのリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<UserVO>> getPages(@RequestBody QueryUserVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Call Driver Page List - ドライバーのリスト")
    @PostMapping("/callDriver/list")
    public ResultVO<ListVO<UserVO>> getPagesByCallDriver(@RequestBody QueryUserVO queryVO) {
        return ResultVO.success(service.getPagesByCallDriver(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit User - 追加・編集 ユーザー")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "InsertOrUpdateUserVO", description = "追加・編集 ユーザー", required = true))
    public ResultVO<UserVO> insertOrUpdate(@Validated @RequestBody IUUserVO iuUserVO) {
        return ResultVO.success(service.insertOrUpdate(iuUserVO));
    }

    @ResponseBody
    @Operation(summary = "ResetPwd User - パスワード変更")
    @PostMapping(value = "/resetPwd")
    @Parameters(@Parameter(name = "restPwdVO", description = "追加・編集 ユーザー", required = true))
    public ResultVO<Boolean> resetPwd(@Validated @RequestBody ResetPwdVO restPwdVO) {
        return ResultVO.success(service.resetPwd(restPwdVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit UserRest - 追加・編集 休暇")
    @PostMapping(value = "/rest")
    @Parameters(@Parameter(name = "iuUserRestVO", description = "追加・編集 休暇", required = true))
    public ResultVO<Boolean> rest(@Validated @RequestBody IUUserRestVO iuUserRestVO) {
        return ResultVO.success(service.rest(iuUserRestVO));
    }

    @ResponseBody
    @Operation(summary = "Delete User - 物理削除 ユーザー")
    @PostMapping("/restCancel/{restId}")
    public ResultVO<Boolean> restCancel(@PathVariable String restId) {
        return ResultVO.success(service.restCancel(restId));
    }

    @ResponseBody
    @Operation(summary = "batchRest - アップロード")
    @PostMapping("/batchRest")
    @Parameters({ @Parameter(name = "key", description = "key", required = true),
            @Parameter(name = "file", description = "ファイル", required = true) })
    public ResultVO<Boolean> batchRest(@RequestParam MultipartFile file, String key) {
        if (!FileUploadUtil.checkPhoto(file) && !FileUploadUtil.checkFile(file)) {
            throw new BaseException(EnumUploadCheck.IMAGE_FORMAT);
        }
        return ResultVO.success(service.batchRest(key, file));
    }

    @ResponseBody
    @Operation(summary = "Export ユーザー")
    @PostMapping(value = "/export")
    @Parameters(@Parameter(name = "queryVO", description = "ユーザー", required = true))
    public void export(@Validated @RequestBody QueryUserVO queryVO) {
        service.export(queryVO);
    }

    @ResponseBody
    @Operation(summary = "Delete User - 削除 ユーザー")
    @DeleteMapping("/delete/{id}")
    public ResultVO<Boolean> delete(@PathVariable String id) {
        return ResultVO.success(service.delete(id));
    }

    @ResponseBody
    @Operation(summary = "Delete User - 物理削除 ユーザー")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

    @ResponseBody
    @Operation(summary = "Audit User - 審査 ユーザー")
    @PutMapping("/audit/{id}")
    public ResultVO<Boolean> audit(@PathVariable String id) {
        return ResultVO.success(service.audit(id));
    }

}
