package com.cbs.oukasystem.vo.out.base;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(name = "DictItemVO")
@NoArgsConstructor
@AllArgsConstructor
public class DictItemVO extends NormalVO {

    @Schema(name = "dictId")
    private String dictId;

    @Schema(name = "dictCode")
    private String dictCode;

    @Schema(name = "itemName")
    private String itemName;

    @Schema(name = "itemCode")
    private String itemCode;

    @Schema(name = "remark")
    private String remark;
}
