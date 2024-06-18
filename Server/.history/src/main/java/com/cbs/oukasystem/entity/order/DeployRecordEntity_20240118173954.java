package com.cbs.oukasystem.entity.order;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.common.BusinessEnum.EnumTargetType;
import com.cbs.oukasystem.entity.OperatorEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "order_deploy_record") // JPA-Hibernate
@NoArgsConstructor
@AllArgsConstructor
public class DeployRecordEntity extends OperatorEntity {

    @Schema(name = "orderId")
    private String orderId;

    @Schema(name = "orderNo")
    private String orderNo;

    @Schema(name = "newId")
    private String newId;

    @Schema(name = "newNo")
    private String newNo;

    @Schema(name = "newName")
    private String newName;

    @Schema(name = "deployType")
    private EnumTargetType deployType;

    @Schema(name = "remark", description = "備考")
    private String remark;

}
