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

    @Schema(name = "goodsType", description = "备品类型")
    private String goodsType;

    @Schema(name = "goodsCount", description = "备品数量")
    private int goodsCount;

    @Schema(name = "remark", length = 500, description = "备注")
    private String remark;

}
