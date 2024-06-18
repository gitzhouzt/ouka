package com.cbs.oukasystem.controller.car;

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

import com.cbs.oukasystem.service.car.CarRepairService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.car.IUCarRepairVO;
import com.cbs.oukasystem.vo.in.car.QueryCarRepairVO;
import com.cbs.oukasystem.vo.out.car.CarRepairVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "車両修理 CarRepairApi")
@RequestMapping("/api/car/repair")
@CrossOrigin
@RestController
public class CarRepairController {

    @Autowired
    private CarRepairService service;

    @ResponseBody
    @Operation(summary = "CarRepairDetail -車両修理情報")
    @GetMapping("/detail/{id}")
    public ResultVO<CarRepairVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All CarRepair - すべての車両修理")
    @GetMapping("/all")
    public ResultVO<List<CarRepairVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "CarRepair Page List -車両修理のリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<CarRepairVO>> getPages(@RequestBody QueryCarRepairVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit CarRepair - 追加・編集車両修理")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "iuCarRepairVO", description = "追加・編集車両修理", required = true))
    public ResultVO<CarRepairVO> insertOrUpdate(@Validated @RequestBody IUCarRepairVO iuCarRepairVO) {
        return ResultVO.success(service.addOrEdit(iuCarRepairVO));
    }

    @ResponseBody
    @Operation(summary = "Delete CarRepair - 削除車両修理")
    @DeleteMapping("/delete/{id}")
    public ResultVO<Boolean> delete(@PathVariable String id) {
        return ResultVO.success(service.delete(id));
    }

    @ResponseBody
    @Operation(summary = "Delete CarRepair - 物理削除車両修理")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

    @ResponseBody
    @Operation(summary = "Audit CarRepair - 審査車両修理")
    @PutMapping("/audit/{id}")
    public ResultVO<Boolean> audit(@PathVariable String id) {
        return ResultVO.success(service.audit(id));
    }
}
