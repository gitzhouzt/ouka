package com.cbs.oukasystem.vo.out.user;

import java.util.Date;
import java.util.List;

import com.cbs.oukasystem.vo.NormalVO;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(name = "LogDetailsVO")
@NoArgsConstructor
@AllArgsConstructor
public class LogDetailsVO extends NormalVO {

    @Schema(name = "userLogVO")
    UserLogVO userLogVO;

    @Schema(name = "logRecordVOs")
    List<LogRecordVO> logRecordVOs;

}
