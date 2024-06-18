package com.cbs.oukasystem.vo.in.sys;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IURoleVO extends NormalVO {

    @Schema(name = "roleName")
    private String roleName;

    @Schema(name = "roleCode")
    private String roleCode;

    @Schema(name = "permissions", description = "パーミッション")
    private String permissions;

    @Schema(name = "remark", description = "備考")
    private String remark;
}
