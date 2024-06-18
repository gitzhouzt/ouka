package com.cbs.oukasystem.vo.in.base;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUDictItemVO extends NormalVO {
    @Schema(name = "dictId")
    private String dictId;

    @Schema(name = "itemName")
    private String itemName;

    @Schema(name = "itemCode")
    private String itemCode;

    @Schema(name = "remark")
    private String remark;
}
