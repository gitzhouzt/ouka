package com.cbs.oukasystem.vo.in.user;

import com.cbs.oukasystem.vo.QueryVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class QueryUserRestVO extends QueryVO {

    @Schema(name = "userRole", description = "役職", example = "ADMIN,STAFF")
    private String userRole;

}
