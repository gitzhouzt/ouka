package com.cbs.oukasystem.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class NormalVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "Id", description = "id", example = "0f525d1a-6509-0512-2988-3a01687fd77f")
    private String id;

    @Schema(name = "lang", description = "lang")
    private String lang;

}
