package com.cbs.oukasystem.vo.in.base;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUConfigVO extends NormalVO {
    private String parentId;

    private String label;

    private String value;

    private String remark;
}
