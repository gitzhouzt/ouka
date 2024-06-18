package com.cbs.oukasystem.vo.in.order;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUOrderGoodsVO extends NormalVO {
    @Schema(name = "orderId")
    private String orderId;

    @Schema(name = "orderNo")
    private String orderNo;

    @Schema(name = "fileName")
    private String fileName;

    @Schema(name = "fileUrl")
    private String fileUrl;

    @Schema(name = "remark")
    private String remark;

}
