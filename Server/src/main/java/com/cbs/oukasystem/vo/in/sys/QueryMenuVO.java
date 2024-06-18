package com.cbs.oukasystem.vo.in.sys;

import com.cbs.oukasystem.vo.QueryVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class QueryMenuVO extends QueryVO {

    @Schema(name = "name", description = "メニュー name")
    private String name;

    @Schema(name = "title", description = "メニュー title")
    private String title;
}
