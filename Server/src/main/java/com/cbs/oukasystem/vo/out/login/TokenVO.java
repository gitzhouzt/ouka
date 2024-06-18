package com.cbs.oukasystem.vo.out.login;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(name = "TokenVO")
@AllArgsConstructor
@NoArgsConstructor
public class TokenVO extends NormalVO {

    @Schema(name = "token", description = "トークン")
    private String token;

    @Schema(name = "tokenRefresh", description = "リフレッシュのトークン")
    private String tokenRefresh;

}
