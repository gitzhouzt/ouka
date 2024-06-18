package com.cbs.oukasystem.vo.out.order;

import java.util.List;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class DeployDetailsVO extends NormalVO {

    @Schema(name = "orderNo")
    private String orderNo;

    @Schema(name = "deployRecordVOs", description = "詳細")
    private List<DeployRecordVO> deployRecordVOs;

}
