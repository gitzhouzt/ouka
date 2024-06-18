package com.cbs.oukasystem.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.cbs.oukasystem.service.common.CommonService;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.common.IUInitAdminVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "共用 Common")
@RequestMapping("/api/common")
@CrossOrigin
@RestController
public class CommonController {

    @Autowired
    private CommonService commonService;

    @ResponseBody
    @Operation(summary = "Upload - アップロード")
    @PostMapping("/upload")
    @Parameters({ @Parameter(name = "key", description = "key", required = true),
            @Parameter(name = "file", description = "ファイル", required = true) })
    public ResultVO<String> upload(@RequestParam MultipartFile file, String key) {
        if (!FileUploadUtil.checkPhoto(file) && !FileUploadUtil.checkFile(file)) {
            throw new BaseException(EnumUploadCheck.IMAGE_FORMAT);
        }
        return ResultVO.success(commonService.upload(key, file));
    }

    @ResponseBody
    @Operation(summary = "initAdmin - 初期化ADMIN")
    @PostMapping("/initAdmin")
    @Parameters(@Parameter(name = "initAdminVO", description = "初期化ADMIN", required = true))
    public ResultVO<Boolean> initAdmin(@Validated @RequestBody IUInitAdminVO initAdminVO) {
        return ResultVO.success(commonService.initAdmin(initAdminVO));
    }

}
