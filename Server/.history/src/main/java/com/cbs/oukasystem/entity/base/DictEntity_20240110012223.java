package com.cbs.oukasystem.entity.base;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.entity.NormalEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "base_dict") // JPA-Hibernate
public class DictEntity extends NormalEntity {

    @Schema(name = "typeName")
    private String typeName;

    @Schema(name = "typeCode")
    private String typeCode;

    @Schema(name = "dictName")
    private String dictName;

    @Schema(name = "dictCode")
    private String dictCode;

    @Schema(name = "remark")
    private String remark;
}
