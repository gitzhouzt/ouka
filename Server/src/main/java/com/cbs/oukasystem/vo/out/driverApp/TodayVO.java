package com.cbs.oukasystem.vo.out.driverApp;

import java.util.List;

import com.cbs.oukasystem.vo.NormalVO;
import com.cbs.oukasystem.vo.out.operate.CallVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(name = "TodayVO")
@NoArgsConstructor
@AllArgsConstructor
public class TodayVO extends NormalVO {

    @Schema(name = "userName", description = "司机编号")
    private String userNo;

    @Schema(name = "userName", description = "司机名称")
    private String userName;

    @Schema(name = "today", description = "今日日期")
    private String today;

    @Schema(name = "orderCnt", description = "订单数")
    private long orderCnt;

    @Schema(name = "todayOrderVOs", description = "车辆（复数")
    private List<TodayOrderVO> todayOrderVOs;

    @Schema(name = "callVOs", description = "点呼")
    private List<CallVO> callVOs;

    @Schema(name = "amCall")
    private int amCall;

    @Schema(name = "pmCall")
    private int pmCall;

    @Schema(name = "log")
    private int log;

}
