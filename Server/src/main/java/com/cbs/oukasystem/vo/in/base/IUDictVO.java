package com.cbs.oukasystem.vo.in.base;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUDictVO extends NormalVO {

    @Schema(name = "dictName")
    private String dictName;

    @Schema(name = "dictCode")
    private String dictCode;

    @Schema(name = "remark")
    private String remark;
}
