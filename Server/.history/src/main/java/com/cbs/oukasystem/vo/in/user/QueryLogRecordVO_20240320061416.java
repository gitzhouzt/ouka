package com.cbs.oukasystem.vo.in.user;

import com.cbs.oukasystem.vo.QueryVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class QueryLogRecordVO extends QueryVO {

    @Schema(name = "userId")
    private String userId;

    @Schema(name = "o")
    private String logId;

}
