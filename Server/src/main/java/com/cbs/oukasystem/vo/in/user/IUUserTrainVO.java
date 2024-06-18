package com.cbs.oukasystem.vo.in.user;

import java.util.Date;

import com.cbs.oukasystem.entity.user.UserEntity;
import com.cbs.oukasystem.vo.NormalVO;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUUserTrainVO extends NormalVO {

    @Schema(name = "userId", description = "Id", example = "userId")
    private String userId;

    @Schema(name = "user", description = "user", example = "user")
    private UserEntity user = new UserEntity();

    @Schema(name = "trainYear", description = "培训年份")
    private String trainYear;

    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+9")
    @Schema(name = "trainDate1", description = "第一次培训日期")
    private Date trainDate1;

    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+9")
    @Schema(name = "trainDate2", description = "第二次培训日期")
    private Date trainDate2;

    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+9")
    @Schema(name = "trainDate3", description = "第三次培训日期")
    private Date trainDate3;

    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+9")
    @Schema(name = "trainDate4", description = "第四次培训日期")
    private Date trainDate4;
}
