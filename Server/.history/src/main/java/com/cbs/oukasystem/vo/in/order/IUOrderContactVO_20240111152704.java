package com.cbs.oukasystem.vo.in.order;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUOrderContactVO extends NormalVO {

    /*
     * 联系方式
     */
    @Schema(name = "orderNo", description = "注文番号")
    private String orderNo;

    @Schema(name = "contactMethod", description = "联系方式")
    private String contactMethod;

    @Schema(name = "contactMethodCode", description = "联系方式Code")
    private String contactMethodCode;

    @Schema(name = "content", description = "联系内容")
    private String content;

}
