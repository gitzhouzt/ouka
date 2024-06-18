package com.cbs.oukasystem.entity.base;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.entity.NormalEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "base_dict_item") // JPA-Hibernate
public class DictItemEntity extends NormalEntity {

    @Schema(name = "typeId")
    private String dictId;

    @Schema(name = "dictCode")
    private String dictCode;

    @Schema(name = "itemName")
    private String itemName;

    @Schema(name = "itemCode")
    private String itemCode;

    @Schema(name = "remark")
    private String remark;
}
