package com.cbs.oukasystem.entity.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.entity.OperatorEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "user_log_record") // JPA-Hibernate
public class LogRecordEntity extends OperatorEntity {

    @Schema(name = "logId", description = "logId")
    private String logId;

    @Schema(name = "userId")
    private String userId;

    @Schema(name = "start", description = "出発地")
    private String start;

    @Schema(name = "startTime", description = "出発地時間")
    private Date startTime;

    @Schema(name = "end", description = "到着地")
    private String end;

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
