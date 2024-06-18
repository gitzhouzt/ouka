package com.cbs.oukasystem.vo.in.user;

import com.cbs.oukasystem.vo.QueryVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUUserRestVO extends QueryVO {

    @Schema(name = "userRole", description = "役職", example = "ADMIN,STAFF")
    private String userRole;

    @Schema(name = "isAdmin", description = "是否管理员")
    private Boolean isAdmin;
}
