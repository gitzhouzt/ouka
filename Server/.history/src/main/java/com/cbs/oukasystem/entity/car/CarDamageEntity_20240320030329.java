package com.cbs.oukasystem.entity.car;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cbs.oukasystem.common.BusinessEnum.EnumCarStatus;
import com.cbs.oukasystem.entity.OperatorEntity;
import com.cbs.oukasystem.entity.order.OrderEntity;

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

    @Schema(name = "carId", description = "車両")
    private String carId;

    @Column(name = "order_id", insertable = false, updatable = false, nullable = true)
    @Schema(name = "orderId")
    private String orderId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    @Schema(name = "order")
    private OrderEntity order = new OrderEntity();

    @Column(name = "remark")
    @Schema(name = "remark", description = "備考")
    private String remark;

}
