package com.cbs.oukasystem.vo.in.user;

import java.util.Date;

import com.cbs.oukasystem.vo.NormalVO;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IULogRecordVO extends NormalVO {

    @Schema(name = "date", description = "date")
    private String date;

    @Schema(name = "logId", description = "logId")
    private String logId;

    @Schema(name = "userId")
    private String userId;

    @Schema(name = "start", description = "出発地")
    private String start;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "startTime", description = "出発地時間")
    private Date startTime;

    @Schema(name = "end", description = "到着地")
    private String end;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "endTime", description = "到着地時間")
    private Date endTime;

    @Schema(name = "distance", description = "距離")
    private Double distance;

    @Schema(name = "jissha", description = "実車")
    private Double jissha;

    @Schema(name = "kaisou", description = "回送")
    private Double kaisou;

    @Schema(name = "rest", description = "休憩時間")
    private String rest;

    @Schema(name = "restNum", description = "休憩時間数")
    private String restNum;

    @Schema(name = "remark", description = "備考")
    private String remark;
}
