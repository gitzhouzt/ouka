package com.cbs.oukasystem.entity.finance;

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
@Table(name = "finance_deposit") // JPA-Hibernate
@NoArgsConstructor
@AllArgsConstructor
public class DepositEntity extends OperatorEntity {

    @Schema(name = "orderId")
    private String orderId;

    @Schema(name = "orderNo")
    private String orderNo;

    @Schema(name = "amount1", description = "立替食事代")
    private int amount1;

    @Schema(name = "amount2", description = "立替ホテル代")
    private int amount2;

    @Schema(name = "amount3", description = "入場税")
    private int amount3;

    @Schema(name = "amount4", description = "駐車代")
    private int amount4;

    @Schema(name = "amount5", description = "ETC料金")
    private int amount5;

    @Schema(name = "amount6", description = "有料道路料金")
    private int amount6;

    @Schema(name = "amount7", description = "入門料チケット")
    private int amount7;

    @Schema(name = "amount8", description = "水代")
    private int amount8;

    @Schema(name = "amount9", description = "残業代")
    private int amount9;

    @Schema(name = "billingAddress")
    private String billingAddress;

    @Schema(name = "financeTime")
    private Date financeByName;

    @Column(name = "remark", length = 1000)
    @Schema(name = "remark")
    private String remark;

}
