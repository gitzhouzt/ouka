package com.cbs.oukasystem.controller.driverApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cbs.oukasystem.service.driverApp.DriverUserService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.order.QueryOrderVO;
import com.cbs.oukasystem.vo.in.user.IULogRecordVO;
import com.cbs.oukasystem.vo.in.user.IUUserLogVO;
import com.cbs.oukasystem.vo.in.user.QueryUserLogVO;
import com.cbs.oukasystem.vo.out.order.OrderVO;
import com.cbs.oukasystem.vo.out.user.LogRecordVO;
import com.cbs.oukasystem.vo.out.user.UserDriveVO;
import com.cbs.oukasystem.vo.out.user.UserLogVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "ドライバーアプリ DriverApi")
@RequestMapping("/api/driver/user")
@CrossOrigin
@RestController
public class DriverUserController {

    @Autowired
    private DriverUserService service;

    @ResponseBody
    @Operation(summary = "UserDetail -司机情報")
    @GetMapping("/details")
    public ResultVO<UserDriveVO> details() {
        return ResultVO.success(service.getDriverInfo());
    }

    @ResponseBody
    @Operation(summary = "log Page List -注文のリスト")
    @PostMapping("/log/list")
    public ResultVO<ListVO<UserLogVO>> getPages(@RequestBody QueryUserLogVO queryVO) {
        return ResultVO.success(service.getLogs(queryVO));
    }

    @ResponseBody
    @Operation(summary = "写日记 - 追加・編集 ユーザー")
    @PostMapping(value = "/writeLog")
    @Parameters(@Parameter(name = "iuUserLogVO", description = "追加・編集 ユーザー", required = true))
    public ResultVO<UserLogVO> writeLog(@Validated @RequestBody IUUserLogVO iuUserLogVO) {
        return ResultVO.success(service.writeLog(iuUserLogVO));
    }

    @ResponseBody
    @Operation(summary = "写日记 - 追加・編集 ユーザー")
    @PostMapping(value = "/writeLogRecord")
    @Parameters(@Parameter(name = "writeLogRecord", description = "追加・編集 ユーザー", required = true))
    public ResultVO<LogRecordVO> writeLogRecord(@Validated @RequestBody IULogRecordVO iuLogRecordVO) {
        return ResultVO.success(service.writeLogRecord(iuLogRecordVO));
    }
}
