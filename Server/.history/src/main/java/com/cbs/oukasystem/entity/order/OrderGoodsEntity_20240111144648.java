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
@Table(name = "order_goods") // JPA-Hibernate
@NoArgsConstructor
@AllArgsConstructor
public class OrderGoodsEntity extends OperatorEntity {

    /*
     * 订单备品
     */
    @Schema(name = "orderNo", description = "注文番号")
    private String orderNo;

    @Schema(name = "", description = "联系方式")
    private String contactMethod;

    @Schema(name = "contactMethodCode", description = "联系方式Code")
    private String contactMethodCode;

    @Schema(name = "content", description = "联系内容")
    private String content;

}
