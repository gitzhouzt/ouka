package com.cbs.oukasystem.vo.in.login;

import com.cbs.oukasystem.common.BusinessEnum.EnumVerifyMethod;
import com.cbs.oukasystem.common.BusinessEnum.EnumVerifyType;
import com.cbs.oukasystem.vo.QueryVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class QueryVerifyVO extends QueryVO {

    @Schema(name = "userId")
    private String userId;

    @Schema(name = "verifyType")
    private EnumVerifyType verifyType;

    @Schema(name = "verifyMethod")
    private EnumVerifyMethod verifyMethod;

    @Schema(name = "content")
    private String content;
}
