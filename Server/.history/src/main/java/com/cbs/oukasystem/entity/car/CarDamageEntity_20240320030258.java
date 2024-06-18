package com.cbs.oukasystem.entity.car;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.common.BusinessEnum.EnumCarStatus;
import com.cbs.oukasystem.entity.OperatorEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "car_damage") // JPA-Hibernate
@NoArgsConstructor
@AllArgsConstructor
public class CarDamageEntity extends OperatorEntity {

    @Column(name = "carNo")
    @Schema(name = "carNo", description = "車両番号")
    private String carNo;

    @Column(name = "carName")
    @Schema(name = "carName", description = "車両名", example = "車両名")
    private String carName;

    @Column(name = "carPhoto")
    @Schema(name = "carPhoto", description = "車両写真")
    private String carPhoto;

    @Schema(name = "carType")
    private String carType;

    @Schema(name = "carSeat")
    private int carSeat = 0;

    @Schema(name = "city")
    private String city;

    @Schema(name = "plateNum")
    private String plateNum;

    @Schema(name = "carPark")
    private String carPark;

    @Schema(name = "parkingFee")
    private int parkingFee;

    @Column(name = "status")
    @Schema(name = "status")
    private EnumCarStatus status = EnumCarStatus.Standing;

    @Schema(name = "statusName")
    private String statusName = EnumCarStatus.Standing.getMessage();

    @Column(name = "remark")
    @Schema(name = "remark", description = "備考")
    private String remark;

}
