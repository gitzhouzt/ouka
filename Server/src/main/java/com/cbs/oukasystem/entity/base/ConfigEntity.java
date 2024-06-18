package com.cbs.oukasystem.entity.base;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.entity.NormalEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "base_config") // JPA-Hibernate
public class ConfigEntity extends NormalEntity {

    private String parentId;

    private String label;

    private String value;

    private String remark;
}
