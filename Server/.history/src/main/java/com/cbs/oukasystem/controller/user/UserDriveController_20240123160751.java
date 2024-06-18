package com.cbs.oukasystem.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cbs.oukasystem.service.user.UserDriveService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.user.QueryUserDriveVO;
import com.cbs.oukasystem.vo.out.user.UserDriveVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

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
    @Operation(summary = "Delete UserDrive - 物理削除 ドライブ")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

}
