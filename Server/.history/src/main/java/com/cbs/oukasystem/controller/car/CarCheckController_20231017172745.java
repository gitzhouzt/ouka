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

import com.cbs.oukasystem.service.car.CheckService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.car.IUCarCheckVO;
import com.cbs.oukasystem.vo.in.car.QueryCarCheckVO;
import com.cbs.oukasystem.vo.out.car.CarCheckVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "車両点検 CarCheckApi")
@RequestMapping("/api/car/check")
@CrossOrigin
@RestController
public class CarCheckController {

    @Autowired
    private CheckService service;

    @ResponseBody
    @Operation(summary = "CarCheckDetail -車両点検情報")
    @GetMapping("/detail/{id}")
    public ResultVO<CarCheckVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All CarCheck - すべての車両点検")
    @GetMapping("/all")
    public ResultVO<List<CarCheckVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "CarCheck Page List -車両点検のリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<CarCheckVO>> getPages(@RequestBody QueryCarCheckVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit CarCheck - 追加・編集車両点検")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "iuCarCheckVO", description = "追加・編集車両点検", required = true))
    public ResultVO<CarCheckVO> insertOrUpdate(@Validated @RequestBody IUCarCheckVO iuCarCheckVO) {
        return ResultVO.success(service.addOrEdit(iuCarCheckVO));
    }

    @ResponseBody
    @Operation(summary = "Delete CarCheck - 削除車両点検")
    @DeleteMapping("/delete/{id}")
    public ResultVO<Boolean> delete(@PathVariable String id) {
        return ResultVO.success(service.delete(id));
    }

    @ResponseBody
    @Operation(summary = "Delete CarCheck - 物理削除車両点検")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

    @ResponseBody
    @Operation(summary = "Audit CarCheck - 審査車両点検")
    @PutMapping("/audit/{id}")
    public ResultVO<Boolean> audit(@PathVariable String id) {
        return ResultVO.success(service.audit(id));
    }
}
