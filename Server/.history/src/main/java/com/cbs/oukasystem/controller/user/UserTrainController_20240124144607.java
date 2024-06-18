package com.cbs.oukasystem.controller.user;

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

import com.cbs.oukasystem.service.user.UserTrainService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.user.IUUserTrainVO;
import com.cbs.oukasystem.vo.in.user.QueryUserTrainVO;
import com.cbs.oukasystem.vo.out.user.UserTrainVO;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "トレーニング UserTrainApi")
@RequestMapping("/api/driver")
@CrossOrigin
@RestController
public class UserTrainController {

    @Autowired
    private UserTrainService service;

    @ResponseBody
    @Operation(summary = "UserTrainDetail - トレーニング情報")
    @GetMapping("/detail/{id}")
    public ResultVO<UserTrainVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All UserTrains - すべてのトレーニング ")
    @GetMapping("/all")
    public ResultVO<List<UserTrainVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "UserTrains Page List - トレーニングのリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<UserTrainVO>> getPages(@RequestBody QueryUserTrainVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit User - 追加・編集 トレーニング")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "iuUserTrainVO", description = "追加・編集 トレーニング", required = true))
    public ResultVO<UserTrainVO> addOrEdit(@Validated @RequestBody IUUserTrainVO iuUserTrainVO) {
        return ResultVO.success(service.addOrEdit(iuUserTrainVO));
    }

    @ResponseBody
    @Operation(summary = "Delete UserTrain - 物理削除 トレーニング")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

}
