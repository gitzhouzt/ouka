package com.cbs.oukasystem.vo.out.car;

import java.util.Date;

import com.cbs.oukasystem.common.CommonEnum.EnumStatus;
import com.cbs.oukasystem.vo.NormalVO;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "CarCheckVO") // swagger3
@Data // lombok
@AllArgsConstructor
@NoArgsConstructor
public class CarCheckVO extends NormalVO {

    @Schema(name = "carId", description = "車両")
    private String carId;

    @Schema(name = "carNo", description = "車両番号")
    private String carNo;

    @Schema(name = "year", description = "当前年度")
    private String year;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "yearCheckDate", description = "年检日期")
    private Date yearCheckDate;

    @Schema(name = "yearCheckStatus", description = "年检状态")
    private Boolean yearCheckStatus;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "monthCheckDate1", description = "点检日期1")
    private Date monthCheckDate1;

    @Schema(name = "checkStatus1", description = "点检1状态")
    private Boolean checkStatus1;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "monthCheckDate2", description = "点检日期2")
    private Date monthCheckDate2;

    @Schema(name = "checkStatus2", description = "点检2状态")
    private Boolean checkStatus2;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "monthCheckDate3", description = "点检日期3")
    private Date monthCheckDate3;

    @Schema(name = "checkStatus3", description = "点检3状态")
    private Boolean checkStatus3;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "monthCheckDate4", description = "点检日期4")
    private Date monthCheckDate4;

    @Schema(name = "checkStatus4", description = "点检2状态")
    private EnumStatus checkStatus4 = EnumStatus.Waiting;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "lastYearCheck", description = "上年度年检")
    private Date lastYearCheck;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "lastMonthCheck", description = "上年度最后一次点检")
    private Date lastMonthCheck;

}
