package com.cbs.oukasystem.vo.in.order;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class OrderCancelVO extends NormalVO {

    @Schema(name = "companyRemark", description = "会社備考")
    private String companyRemark;

}
