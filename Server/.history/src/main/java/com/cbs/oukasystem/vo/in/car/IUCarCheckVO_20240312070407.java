package com.cbs.oukasystem.vo.in.car;

import java.util.Date;

import com.cbs.oukasystem.common.CommonEnum.EnumStatus;
import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema
@NoArgsConstructor
@AllArgsConstructor
public class IUCarCheckVO extends NormalVO {

    @Schema(name = "carId", description = "車両")
    private String carId;

    @Schema(name = "carNo", description = "車両番号")
    private String carNo;

    @Schema(name = "year", description = "当前年度")
    private String year;

    @Schema(name = "yearCheckDate", description = "年检日期")
    private Date yearCheckDate;

    @Schema(name = "monthCheckDate1", description = "点检日期1")
    private Date monthCheckDate1;

    @Schema(name = "checkStatus1", description = "点检1状态")
    private EnumStatus checkStatus1 = EnumStatus.Waiting;

    @Schema(name = "monthCheckDate2", description = "点检日期2")
    private Date monthCheckDate2;

    @Schema(name = "checkStatus2", description = "点检2状态")
    private EnumStatus checkStatus2 = EnumStatus.Waiting;

    @Schema(name = "monthCheckDate3", description = "点检日期3")
    private Date monthCheckDate3;

    @Schema(name = "checkStatus3", description = "点检2状态")
    private EnumStatus checkStatus3 = EnumStatus.Waiting;

    @Schema(name = "monthCheckDate4", description = "点检日期4")
    private Date monthCheckDate4;

    @Schema(name = "checkStatus4", description = "点检2状态")
    private EnumStatus checkStatus4 = EnumStatus.Waiting;

    @Schema(name = "lastYearCheck", description = "上年度年检")
    private Date lastYearCheck;

    @Schema(name = "lastMonthCheck", description = "上年度最后一次点检")
    private Date lastMonthCheck;

}
