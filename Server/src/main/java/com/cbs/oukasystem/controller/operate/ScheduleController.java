package com.cbs.oukasystem.controller.operate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cbs.oukasystem.service.operate.ScheduleService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.operate.QueryScheduleVO;
import com.cbs.oukasystem.vo.out.operate.ScheduleVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Api(tags = "スケジュール ScheduleApi")
@RequestMapping("/api/operate/schedule")
@CrossOrigin
@RestController
public class ScheduleController {

    @Autowired
    private ScheduleService service;

    @ResponseBody
    @Operation(summary = "ScheduleDetail -スケジュール情報")
    @GetMapping("/detail/{id}")
    public ResultVO<ScheduleVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All Schedule - すべてのスケジュール")
    @GetMapping("/all")
    public ResultVO<List<ScheduleVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "schedule -スケジュールのリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<ScheduleVO>> schedule(@RequestBody QueryScheduleVO queryVO) {
        return ResultVO.success(service.getSchedule(queryVO));
    }

    @ResponseBody
    @Operation(summary = "schedule -スケジュールのリスト")
    @PostMapping("/workByMonth/list")
    public ResultVO<ListVO<ScheduleVO>> workByMonth(@RequestBody QueryScheduleVO queryVO) {
        return ResultVO.success(service.getWorkByMonth(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Delete Schedule - 物理削除スケジュール")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

}
