package com.cbs.oukasystem.controller.order;

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

import com.cbs.oukasystem.service.order.DeployRecordService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.order.IUDeployRecordVO;
import com.cbs.oukasystem.vo.in.order.QueryDeployRecordVO;
import com.cbs.oukasystem.vo.out.order.DeployRecordVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "分配変更 DeployApi")
@RequestMapping("/api/deploy")
@CrossOrigin
@RestController
public class DeployRecordController {

    @Autowired
    private DeployRecordService service;

    @ResponseBody
    @Operation(summary = "DeployDetail -分配変更情報")
    @GetMapping("/detail/{id}")
    public ResultVO<DeployRecordVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "All Deploy - すべての分配変更")
    @GetMapping("/all")
    public ResultVO<List<DeployRecordVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "From Deploy Page List -分配変更のリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<DeployRecordVO>> getPages(@RequestBody QueryDeployRecordVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit Deploy - 追加・編集分配変更")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "iuDeployVO", description = "追加・編集分配変更", required = true))
    public ResultVO<DeployRecordVO> insertOrUpdate(@Validated @RequestBody IUDeployRecordVO iuDeployVO) {
        return ResultVO.success(service.addOrEdit(iuDeployVO));
    }

    @ResponseBody
    @Operation(summary = "Delete Deploy - 削除分配変更")
    @DeleteMapping("/delete/{id}")
    public ResultVO<Boolean> delete(@PathVariable String id) {
        return ResultVO.success(service.delete(id));
    }

    @ResponseBody
    @Operation(summary = "Delete Deploy - 物理削除分配変更")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

    @ResponseBody
    @Operation(summary = "Audit Deploy - 審査分配変更")
    @PutMapping("/audit/{id}")
    public ResultVO<Boolean> audit(@PathVariable String id) {
        return ResultVO.success(service.audit(id));
    }
}
