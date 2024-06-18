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
@Table(name = "order_files") // JPA-Hibernate
@NoArgsConstructor
@AllArgsConstructor
public class OrderFilesEntity extends OperatorEntity {

    @Schema(name = "orderId")
    private String orderId;

    @Schema(name = "orderNo")
    private String orderNo;

    @Schema(name = "fileName")
    private String fileName;

    @Schema(name = "fileUrl")
    private String fileUrl;

    @Schema(name = "remark")
    private String remark;

}
