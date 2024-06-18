package com.cbs.oukasystem.entity.operate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.common.BusinessEnum.EnumTargetType;
import com.cbs.oukasystem.entity.NormalEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "schedule_master") // JPA-Hibernate
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleEntity extends NormalEntity {

    @Schema(name = "targetId", description = "目标id 车或人")
    private String targetId;

    @Schema(name = "targetNo", description = "目标番号")
    private String targetNo;

    @Schema(name = "targetName", description = "目标名称")
    private String targetName;

    @Schema(name = "targetType", description = "目标类型 车或人")
    private EnumTargetType actionTarget;

    @Schema(name = "actionTargetCode", description = "actionTargetCode")
    private String actionTargetCode;

    @Schema(name = "actionId", description = "actionId")
    private String actionId;

    @Schema(name = "actionType", description = "动作类型 订单 休息 维修 点检等")
    private String actionType;

    @Schema(name = "actionTypeCode", description = "动作类型")
    private String actionTypeCode;

    @Schema(name = "workDate", description = "作業日")
    private String workDate;

    @Schema(name = "workTime", description = "作業時間")
    private String workTime;

    @Schema(name = "remark", description = "備考")
    private String remark;

}
