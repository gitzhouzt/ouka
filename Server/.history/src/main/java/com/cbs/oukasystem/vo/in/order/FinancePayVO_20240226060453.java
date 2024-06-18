package com.cbs.oukasystem.vo.in.order;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class FinancePayVO extends NormalVO {

    @Schema(name = "payIds")
    private String[] payIds;

}
