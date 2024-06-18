package com.cbs.oukasystem.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cbs.oukasystem.service.common.TestService;
import com.cbs.oukasystem.vo.ResultVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;

@Api(tags = "测试 Test")
@RequestMapping("/api/test")
@CrossOrigin
@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @ResponseBody
    @Operation(summary = "i18nTest")
    @GetMapping("/i18nTest")
    public ResultVO<String> i18nTest() {
        return ResultVO.success(testService.i18nTest());
    }

    @ResponseBody
    @Operation(summary = "pdf ")
    @PostMapping(value = "/pdf")
    public void pdf() {
        service.pdf();
    }

}
