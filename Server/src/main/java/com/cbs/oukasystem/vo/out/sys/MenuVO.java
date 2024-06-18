package com.cbs.oukasystem.vo.out.sys;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(name = "MenuVO")
@AllArgsConstructor
@NoArgsConstructor
public class MenuVO extends NormalVO {

    @Schema(name = "name", description = "メニュー name")
    private String name;

    @Schema(name = "title", description = "メニュー title")
    private String title;

    @Schema(name = "icon", description = "メニューアイコン")
    private String icon;

    @Schema(name = "component", description = "コンポーネント")
    private String component;

    @Schema(name = "parentID", description = "上層メニューID")
    private String parentId;

    @Schema(name = "requiresAuth")
    private boolean requiresAuth;

    @Schema(name = "path")
    private String path;

}
