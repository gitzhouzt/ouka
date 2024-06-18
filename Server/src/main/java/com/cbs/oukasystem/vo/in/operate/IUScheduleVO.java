package com.cbs.oukasystem.vo.in.operate;

import java.util.Date;

import com.cbs.oukasystem.common.BusinessEnum.EnumActionType;
import com.cbs.oukasystem.common.BusinessEnum.EnumTargetType;
import com.cbs.oukasystem.vo.NormalVO;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema
@NoArgsConstructor
@AllArgsConstructor
public class IUScheduleVO extends NormalVO {

    @Schema(name = "targetId", description = "目标id 车或人")
    private String targetId;

    @Schema(name = "targetNo", description = "目标番号")
    private String targetNo;

    @Schema(name = "targetName", description = "目标名称")
    private String targetName;

    @Schema(name = "targetType", description = "目标类型 车或人")
    private EnumTargetType targetType;

    @Schema(name = "actionId", description = "动作id 订单id，休息id，维修id等")
    private String actionId;

    @Schema(name = "actionType", description = "动作类型 订单 休息 维修 点检等")
    private EnumActionType actionType;

    @Schema(name = "workDate", description = "作業日")
    private String workDate;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "workTime", description = "作業時間")
    private Date workTime;

    @Schema(name = "remark", description = "備考")
    private String remark;

}