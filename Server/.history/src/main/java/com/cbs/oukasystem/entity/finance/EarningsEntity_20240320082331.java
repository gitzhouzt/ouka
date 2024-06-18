package com.cbs.oukasystem.entity.finance;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.common.BusinessEnum.EnumFinanceStatus;
import com.cbs.oukasystem.entity.OperatorEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "finance_earnings") // JPA-Hibernate
@NoArgsConstructor
@AllArgsConstructor
public class EarningsEntity extends OperatorEntity {

    @Schema(name = "orderId")
    private String orderId;

    @Schema(name = "orderNo")
    private String orderNo;

    @Schema(name = "handlingFee", description = "手续费")
    private int handlingFee;

    @Schema(name = "oilFee", description = "油费")
    private int oilFee;

    @Schema(name = "etc", description = "ETC")
    private int etc;

    @Schema(name = "entrustSalary", description = "委托工资")
    private int entrustSalary;

    @Schema(name = "salary", description = "司机工资")
    private int salary;

    @Schema(name = "amount6", description = "外派金额")
    private int amount6;

    @Schema(name = "amount7", description = "入門料チケット")
    private int amount7;

    @Schema(name = "amount8", description = "水代")
    private int amount8;

    @Schema(name = "amount9", description = "残業代")
    private int amount9;

    @Schema(name = "amount99", description = "その他立替")
    private int amount99;

    @Schema(name = "billingAddress")
    private String billingAddress;

    @Schema(name = "financeTime")
    private Date financeByName;

    @Schema(name = "status")
    private EnumFinanceStatus status;

    @Column(name = "remark", length = 1000)
    @Schema(name = "remark")
    private String remark;

}
