package com.cbs.oukasystem.entity.car;

import java.util.Date;

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

    @Schema(name = "yearCheck", description = "年检")
    private Date yearCheck;

    @Schema(name = "monthCheck1", description = "点检日期1")
    private Date monthCheck1;

    @Schema(name = "monthCheck2", description = "点检日期2")
    private Date monthCheck2;

    @Schema(name = "monthCheck3", description = "点检日期3")
    private Date monthCheck3;

    @Schema(name = "monthCheck4", description = "点检日期4")
    private Date monthCheck4;

    @Schema(name = "lastYearCheck", description = "上年度年检")
    private Date lastYearCheck;

    @Schema(name = "lastMonthCheck", description = "上年度最后一次点检")
    private Date lastMonthCheck;

}
