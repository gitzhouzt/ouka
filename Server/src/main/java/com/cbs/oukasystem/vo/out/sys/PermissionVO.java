package com.cbs.oukasystem.vo.out.sys;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(name = "PermissionVO")
@AllArgsConstructor
@NoArgsConstructor
public class PermissionVO extends NormalVO {

    @Schema(name = "roleId")
    private String roleId;

    @Schema(name = "menuId")
    private String menuId;

    @Schema(name = "actions")
    private String actions;

    @Schema(name = "remark", description = "備考")
    private String remark;

}
