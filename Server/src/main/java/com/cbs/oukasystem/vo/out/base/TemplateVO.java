package com.cbs.oukasystem.vo.out.base;

import com.cbs.oukasystem.common.BusinessEnum.EnumTemplateType;
import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(name = "TemplateVO")
@NoArgsConstructor
@AllArgsConstructor
public class TemplateVO extends NormalVO {
    private String parentId;

    private String label;

    private String url;

    private EnumTemplateType type;

    private String remark;

}
