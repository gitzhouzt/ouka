package com.cbs.oukasystem.vo.in.operate;

import java.util.Date;

import com.cbs.oukasystem.common.BusinessEnum.EnumActionType;
import com.cbs.oukasystem.common.BusinessEnum.EnumDateType;
import com.cbs.oukasystem.common.BusinessEnum.EnumTargetType;
import com.cbs.oukasystem.vo.QueryVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class QueryScheduleVO extends QueryVO {

    @Schema(name = "targetType", description = "目标类型 车或人")
    private EnumTargetType targetType;

    @Schema(name = "actionId", description = "动作id 订单id，休息id，维修id等")
    private String actionId;

    @Schema(name = "actionType", description = "动作类型 订单 休息 维修 点检等")
    private EnumActionType actionType;

    @Schema(name = "workDate", description = "作業日")
    private String workDate;

    @Schema(name = "workTime", description = "作業時間")
    private Date workTime;

}
