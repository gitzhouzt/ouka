package com.cbs.oukasystem.entity.car;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.common.CommonEnum.EnumStatus;
import com.cbs.oukasystem.entity.NormalEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "car_check") // JPA-Hibernate
@NoArgsConstructor
@AllArgsConstructor
public class CarCheckEntity extends NormalEntity {

    @Schema(name = "carId", description = "車両")
    private String carId;

    @Schema(name = "carNo", description = "車両番号")
    private String carNo;

    @Schema(name = "year", description = "当前年度")
    private String year;

    @Schema(name = "yearCheckDate", description = "年检日期")
    private Date yearCheckDate;

    @Schema(name = "yearCheckStatus", description = "年检状态")
    private EnumStatus yearCheckStatus = EnumStatus.Waiting;

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

    @Schema(name = "checkStatus3", description = "点检3状态")
    private EnumStatus checkStatus3 = EnumStatus.Waiting;

    @Schema(name = "lastYearCheck", description = "上年度年检")
    private Date lastYearCheck;

    @Schema(name = "lastMonthCheck", description = "上年度最后一次点检")
    private Date lastMonthCheck;

}
