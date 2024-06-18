package com.cbs.oukasystem.vo.out.login;

import com.cbs.oukasystem.common.BusinessEnum.EnumVerifyMethod;
import com.cbs.oukasystem.common.BusinessEnum.EnumVerifyType;
import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(name = "VerifyVO")
@NoArgsConstructor
@AllArgsConstructor
public class VerifyVO extends NormalVO {

    @Schema(name = "userId")
    private String userId;

    @Schema(name = "content")
    private String content;

    @Schema(name = "code")
    private String code;

    @Schema(name = "verifyType")
    private EnumVerifyType verifyType;

    @Schema(name = "verifyMethod")
    private EnumVerifyMethod verifyMethod;

    @Schema(name = "expiration", description = "有効時間")
    private long expiration;

}
