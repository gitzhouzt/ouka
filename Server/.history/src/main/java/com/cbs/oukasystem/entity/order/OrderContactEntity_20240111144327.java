package com.cbs.oukasystem.entity.order;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.common.BusinessEnum.EnumOrderStatus;
import com.cbs.oukasystem.common.BusinessEnum.EnumOrderType;
import com.cbs.oukasystem.entity.OperatorEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "order_contact") // JPA-Hibernate
@NoArgsConstructor
@AllArgsConstructor
public class OrderContactEntity extends OperatorEntity {

    /*
     * 联系方式
     */
    @Schema(name = "orderNo", description = "注文番号")
    private String orderNo;

    @Schema(name = "contactMethod", description = "联系类型")
    private String contactMethod;

    @Schema(name = "typeCode", description = "联系类型")
    private String typeCode;

    @Schema(name = "content", description = "联系内容")
    private String content;

}
