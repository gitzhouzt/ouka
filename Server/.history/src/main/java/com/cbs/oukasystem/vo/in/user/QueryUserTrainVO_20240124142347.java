package com.cbs.oukasystem.vo.in.user;

import com.cbs.oukasystem.vo.QueryVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class QueryUserTrainVO extends QueryVO {

    @Schema(name = "trainYear", description = "培训年份")
    private String trainYear;
}
