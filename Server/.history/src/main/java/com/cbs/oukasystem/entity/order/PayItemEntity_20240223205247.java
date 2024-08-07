package com.cbs.oukasystem.entity.order;

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
@Table(name = "order_pay_item") // JPA-Hibernate
@NoArgsConstructor
@AllArgsConstructor
public class PayItemEntity extends OperatorEntity {

    @Schema(name = "orderId")
    private String orderId;

    @Schema(name = "orderNo")
    private String orderNo;

    @Schema(name = "payItem", description = "收现名目")
    private String payItem;

    @Schema(name = "payItemCode", description = "收现名目")
    private String payItemCode;

    @Schema(name = "amount", description = "日元金额")
    private Double amount;

    @Schema(name = "remark", description = "備考")
    private String remark;

}
