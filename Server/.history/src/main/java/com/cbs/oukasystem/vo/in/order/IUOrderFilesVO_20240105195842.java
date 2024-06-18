package com.cbs.oukasystem.vo.in.order;

import java.util.Date;

import com.cbs.oukasystem.common.BusinessEnum.EnumOrderStatus;
import com.cbs.oukasystem.common.BusinessEnum.EnumOrderType;
import com.cbs.oukasystem.vo.NormalVO;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUOrderFilesVO extends NormalVO {
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
