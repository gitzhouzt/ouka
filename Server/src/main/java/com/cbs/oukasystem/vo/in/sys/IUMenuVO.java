package com.cbs.oukasystem.vo.in.sys;

import javax.validation.constraints.NotBlank;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUMenuVO extends NormalVO {

    @NotBlank(message = "名前を入力してください")
    @Schema(name = "name", description = "メニュー 名前")
    private String name;

    @NotBlank(message = "タイトルを入力してください")
    @Schema(name = "title", description = "メニュー タイトル")
    private String title;

    @Schema(name = "icon", description = "メニューアイコン")
    private String icon;

    @Schema(name = "component", description = "コンポーネント")
    private String component = "basic";

    @Schema(name = "parentID", description = "上層メニューID")
    private String parentId;

    @Schema(name = "requiresAuth")
    private boolean requiresAuth = true;

    @Schema(name = "path")
    private String path;
}
