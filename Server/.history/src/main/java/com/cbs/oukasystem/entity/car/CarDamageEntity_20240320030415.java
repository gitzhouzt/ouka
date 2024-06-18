package com.cbs.oukasystem.entity.car;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

    @Column(name = "car_id", insertable = false, updatable = false, nullable = true)
    @Schema(name = "carId")
    private String carId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "car_id")
    @Schema(name = "car")
    private CarEntity car = new CarEntity();

    @Schema(name = "remark", description = "備考")
    private String images;

    @Schema(name = "remark", description = "備考")
    private String remark;

}
