package com.cbs.oukasystem.vo.in.base;

import com.cbs.oukasystem.common.BusinessEnum.EnumTemplateType;
import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUTemplateVO extends NormalVO {
    private String parentId;

    private String label;

    private String url;

    private EnumTemplateType type;

    private String remark;
}
