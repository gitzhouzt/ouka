package com.cbs.oukasystem.vo.out.base;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(name = "ConfigVO")
@NoArgsConstructor
@AllArgsConstructor
public class ConfigVO extends NormalVO {
    private String parentId;

    private String label;

    private String value;

    private String remark;

}
