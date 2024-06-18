package com.cbs.oukasystem.vo.out.base;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(name = "DictVO")
@NoArgsConstructor
@AllArgsConstructor
public class DictVO extends NormalVO {

    @Schema(name = "dictName")
    private String dictName;

    @Schema(name = "dictCode")
    private String dictCode;

    @Schema(name = "remark")
    private String remark;
}
