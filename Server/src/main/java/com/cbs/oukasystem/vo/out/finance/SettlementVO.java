package com.cbs.oukasystem.vo.out.finance;

import java.util.List;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "SettlementVO") // swagger3
@Data // lombok
@AllArgsConstructor
@NoArgsConstructor
public class SettlementVO extends NormalVO {

    @Schema(name = "ids", description = "ids")
    private List<String> ids;

    @Schema(name = "payMethod", description = "支払い方法")
    private String payMethod;

    @Schema(name = "payMethodCode", description = "支払い方法")
    private String payMethodCode;

    @Schema(name = "bank", description = "銀行名")
    private String bank;

}
