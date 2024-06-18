package com.cbs.oukasystem.vo.in.user;

import com.cbs.oukasystem.vo.QueryVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class QueryLogRecordLogVO extends QueryVO {

    @Schema(name = "logId")
    private String logId;

    @Schema(name = "userId")
    private String userId;

}
