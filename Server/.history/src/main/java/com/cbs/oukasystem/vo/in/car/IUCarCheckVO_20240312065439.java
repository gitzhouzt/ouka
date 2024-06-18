package com.cbs.oukasystem.vo.in.car;

import java.util.Date;

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

    @Schema(name = "yearCheck", description = "年检")
    private Date yearCheck;

    @Schema(name = "monthCheck1", description = "点检1")
    private Date monthCheck1;

    @Schema(name = "monthCheck2", description = "点检2")
    private Date monthCheck2;

    @Schema(name = "monthCheck3", description = "点检3")
    private Date monthCheck3;

    @Schema(name = "monthCheck4", description = "点检4")
    private Date monthCheck4;

    @Schema(name = "lastYearCheck", description = "上年度年检")
    private Date lastYearCheck;

    @Schema(name = "lastMonthCheck", description = "上年度最后一次点检")
    private Date lastMonthCheck;

}
