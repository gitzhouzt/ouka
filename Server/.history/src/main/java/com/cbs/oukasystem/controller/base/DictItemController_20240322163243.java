package com.cbs.oukasystem.controller.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cbs.oukasystem.common.FileUploadUtil;
import com.cbs.oukasystem.common.MessageEnum.EnumUploadCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.service.base.DictItemService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.base.IUDictItemVO;
import com.cbs.oukasystem.vo.in.base.QueryDictItemVO;
import com.cbs.oukasystem.vo.out.base.DictItemVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "辞典Item DictItemItemApi")
@RequestMapping("/api/base/dictItem")
@CrossOrigin
@RestController
public class DictItemController {

    @Autowired
    private DictItemService service;

    @ResponseBody
    @Operation(summary = "DictItemDetail - 辞典情報")
    @GetMapping("/detail/{id}")
    public ResultVO<DictItemVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "DictItem - 辞典情報")
    @GetMapping("/getByCode")
    public ResultVO<DictItemVO> getByCode(@RequestBody QueryDictItemVO queryVO) {
        return ResultVO.success(service.getByCode(queryVO));
    }

    @ResponseBody
    @Operation(summary = "All DictItems - すべての辞典 ")
    @GetMapping("/all")
    public ResultVO<List<DictItemVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "All DictItems - すべての辞典 ")
    @GetMapping("/query")
    public ResultVO<List<DictItemVO>> query(@RequestBody QueryDictItemVO queryVO) {
        return ResultVO.success(service.query(queryVO));
    }

    @ResponseBody
    @Operation(summary = "DictItems Page List - 辞典のリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<DictItemVO>> getPages(@RequestBody QueryDictItemVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "batch - アップロード")
    @PostMapping("/batch")
    @Parameters({ @Parameter(name = "key", description = "key", required = true),
            @Parameter(name = "file", description = "ファイル", required = true) })
    public ResultVO<Boolean> batch(@RequestParam MultipartFile file, String key) {
        return ResultVO.success(service.batch(key, file));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit DictItem - 追加・編集 辞典")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "iuDictItemVO", description = "追加・編集 辞典", required = true))
    public ResultVO<DictItemVO> insertOrUpdate(@Validated @RequestBody IUDictItemVO iuDictItemVO) {
        return ResultVO.success(service.addOrEdit(iuDictItemVO));
    }

    @ResponseBody
    @Operation(summary = "Delete DictItem - 物理削除 辞典")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

}