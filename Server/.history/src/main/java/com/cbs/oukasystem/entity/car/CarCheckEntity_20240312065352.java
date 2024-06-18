package com.cbs.oukasystem.entity.car;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.entity.OperatorEntity;

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
public class CarCheckEntity extends OperatorEntity {

    @Column(name = "carId")
    @Schema(name = "carId", description = "車両")
    private String carId;

    @Column(name = "carNo")
    @Schema(name = "carNo", description = "車両番号")
    private String carNo;

    @Column(name = "yearCheck")
    @Schema(name = "yearCheck", description = "年检")
    private Date yearCheck;

    @Column(name = "monthCheck1")
    @Schema(name = "monthCheck1", description = "点检1")
    private Date monthCheck1;

    @Column(name = "monthCheck2")
    @Schema(name = "monthCheck2", description = "点检2")
    private Date monthCheck2;

    @Column(name = "monthCheck3")
    @Schema(name = "monthCheck3", description = "点检3")
    private Date monthCheck3;

    @Column(name = "monthCheck4")
    @Schema(name = "monthCheck4", description = "点检4")
    private Date monthCheck4;

    @Column(name = "lastYearCheck")
    @Schema(name = "lastYearCheck", description = "上年度年检")
    private Date lastYearCheck;

    @Column(name = "lastMonthCheck")
    @Schema(name = "lastMonthCheck", description = "上年度最后一次点检")
    private Date lastMonthCheck;

}
