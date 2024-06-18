package com.cbs.oukasystem.controller.sys;

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

import com.cbs.oukasystem.service.sys.MenuService;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.sys.IUMenuVO;
import com.cbs.oukasystem.vo.out.sys.MenuVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "メニュー", description = "MenuApi")
@RequestMapping("/api/sys/menu")
@CrossOrigin
@RestController
public class MenuController {

    @Autowired
    private MenuService menuServiceImpl;

    @ResponseBody
    @Operation(summary = "MenuDetail - メニュー情報")
    @GetMapping("/detail/{id}")
    public ResultVO<MenuVO> getById(@PathVariable @Parameter(required = true, description = "id") String id) {
        return menuServiceImpl.getById(id);
    }

    @ResponseBody
    @Operation(summary = "All Menus - すべてのメニュー ")
    @GetMapping("/all")
    public ResultVO<List<MenuVO>> getAll() {
        return menuServiceImpl.getAll();
    }

    @ResponseBody
    @Operation(summary = "Add/Edit Menu - 追加・編集 メニュー")
    @PostMapping(value = "/addOrEdit")
    @Parameters(@Parameter(name = "iuMenuVO", description = "追加・編集 メニュー", required = true))
    public ResultVO<Boolean> insertOrUpdate(@Validated @RequestBody IUMenuVO iuMenuVO) {
        return menuServiceImpl.insertOrUpdate(iuMenuVO);
    }

    @ResponseBody
    @Operation(summary = "Delete Menu - 削除 メニュー")
    @DeleteMapping("/delete/{id}")
    public ResultVO<Boolean> delete(@PathVariable String id) {
        return menuServiceImpl.delete(id);
    }

    @ResponseBody
    @Operation(summary = "Audit Menu - 審査 メニュー")
    @PutMapping("/audit/{id}")
    public ResultVO<Boolean> audit(@PathVariable String id) {
        return menuServiceImpl.audit(id);
    }
}
