package com.cbs.oukasystem.entity.base;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.common.BusinessEnum.EnumTemplateType;
import com.cbs.oukasystem.entity.NormalEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "base_template") // JPA-Hibernate
public class TemplateEntity extends NormalEntity {

    private String parentId;

    private String label;

    private String url;

    private EnumTemplateType type;

    private String remark;
}
