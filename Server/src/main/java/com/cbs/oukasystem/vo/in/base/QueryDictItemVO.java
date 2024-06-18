package com.cbs.oukasystem.vo.in.base;

import com.cbs.oukasystem.vo.QueryVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class QueryDictItemVO extends QueryVO {

    @Schema(name = "dictCode")
    private String dictCode;

    @Schema(name = "itemCode")
    private String itemCode;
}
