package com.cbs.oukasystem.entity.operate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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

    @Column(name = "targetId")
    @Schema(name = "targetId", description = "targetId")
    private String targetId;

    @Column(name = "targetNo")
    @Schema(name = "targetNo", description = "targetNo")
    private String targetNo;

    @Column(name = "targetName")
    @Schema(name = "targetName", description = "targetName")
    private String targetName;

    @Column(name = "actionTarget")
    @Schema(name = "actionTarget", description = "actionTarget")
    private String actionTarget;

    @Column(name = "actionTargetCode")
    @Schema(name = "actionTargetCode", description = "actionTargetCode")
    private String actionTargetCode;

    @Column(name = "actionId")
    @Schema(name = "actionId", description = "actionId")
    private String actionId;

    @Column(name = "actionType")
    @Schema(name = "actionType", description = "actionType")
    private String actionType;

    @Column(name = "actionTypeCode")
    @Schema(name = "actionTypeCode", description = "actionTypeCode")
    private String actionTypeCode;

    @Column(name = "workTime")
    @Schema(name = "workTime", description = "作業時間")
    private String workTime;

    @Column(name = "remark")
    @Schema(name = "remark", description = "備考")
    private String remark;

}
