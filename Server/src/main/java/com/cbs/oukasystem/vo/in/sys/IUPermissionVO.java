package com.cbs.oukasystem.vo.in.sys;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUPermissionVO extends NormalVO {

    @Schema(name = "roleId")
    private String roleId;

    @Schema(name = "menuId")
    private String menuId;

    @Schema(name = "actions")
    private String actions;

    @Schema(name = "remark", description = "備考")
    private String remark;

}
