package com.cbs.oukasystem.entity.car;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.common.CommonEnum.EnumStatus;
import com.cbs.oukasystem.entity.OperatorEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "car_repair") // JPA-Hibernate
@NoArgsConstructor
@AllArgsConstructor
public class CarRepairEntity extends OperatorEntity {

    @Column(name = "carId")
    @Schema(name = "carId", description = "車両番号")
    private String carId;

    @Column(name = "carNo")
    @Schema(name = "carNo", description = "車両番号")
    private String carNo;

    @Column(name = "carName")
    @Schema(name = "carName", description = "車両名", example = "車両名")
    private String carName;

    @Column(name = "carRepairType")
    @Schema(name = "carRepairType")
    private String repairType;

    @Column(name = "repairTime")
    @Schema(name = "repairTime")
    private Date repairTime;

    @Column(name = "status")
    @Schema(name = "status")
    private EnumStatus status = EnumStatus.Filling;

    @Column(name = "remark")
    @Schema(name = "remark", description = "備考")
    private String remark;

}
