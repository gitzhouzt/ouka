package com.cbs.oukasystem.entity.car;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.common.BusinessEnum.EnumCarCheckType;
import com.cbs.oukasystem.common.CommonEnum.EnumStatus;
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

    @Column(name = "year")
    @Schema(name = "year", description = "年", example = "年")
    private int year = 0;

    @Column(name = "month")
    @Schema(name = "month", description = "月", example = "月")
    private int month = 0;

    @Column(name = "checkType")
    @Schema(name = "checkType")
    private EnumCarCheckType checkType = EnumCarCheckType.Check_3_Month;

    @Column(name = "status")
    @Schema(name = "status")
    private EnumStatus status = EnumStatus.Waiting;

    @Column(name = "url")
    @Schema(name = "url", description = "点検証明書", example = "点検証明書")
    private String url;

    @Column(name = "remark")
    @Schema(name = "remark", description = "備考")
    private String remark;

}
