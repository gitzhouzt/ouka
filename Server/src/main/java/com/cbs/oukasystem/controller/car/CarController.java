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

import com.cbs.oukasystem.service.car.CarService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.car.IUCarVO;
import com.cbs.oukasystem.vo.in.car.QueryCarVO;
import com.cbs.oukasystem.vo.out.car.CarVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "車両 CarApi")
@RequestMapping("/api/car")
@CrossOrigin
@RestController
public class CarController {

    @Autowired
    private CarService service;

    @ResponseBody
    @Operation(summary = "CarDetail -車両情報")
    @GetMapping("/detail/{id}")
    public ResultVO<CarVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All Car - すべての車両")
    @GetMapping("/all")
    public ResultVO<List<CarVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "Car Page List -車両のリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<CarVO>> getPages(@RequestBody QueryCarVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit Car - 追加・編集車両")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "iuCarVO", description = "追加・編集車両", required = true))
    public ResultVO<CarVO> addOrEdit(@Validated @RequestBody IUCarVO iuCarVO) {
        return ResultVO.success(service.addOrEdit(iuCarVO));
    }

    @ResponseBody
    @Operation(summary = "Export 車両")
    @PostMapping(value = "/export")
    @Parameters(@Parameter(name = "queryVO", description = "車両", required = true))
    public void export(@Validated @RequestBody QueryCarVO queryVO) {
        service.export(queryVO);
    }

    @ResponseBody
    @Operation(summary = "Delete Car - 削除車両")
    @DeleteMapping("/delete/{id}")
    public ResultVO<Boolean> delete(@PathVariable String id) {
        return ResultVO.success(service.delete(id));
    }

    @ResponseBody
    @Operation(summary = "Delete Car - 物理削除車両")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

    @ResponseBody
    @Operation(summary = "Audit Car - 審査車両")
    @PutMapping("/audit/{id}")
    public ResultVO<Boolean> audit(@PathVariable String id) {
        return ResultVO.success(service.audit(id));
    }

}
