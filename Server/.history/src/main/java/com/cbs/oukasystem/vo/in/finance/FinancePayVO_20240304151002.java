package com.cbs.oukasystem.vo.in.finance;

import java.util.List;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class FinancePayVO extends NormalVO {

    @Schema(name = "payMethod")
    private String payMethod;

    @Schema(name = "bank")
    private String bank;

    @Schema(name = "payIds")
    private List<String> payIds;

}
