package com.cbs.oukasystem.entity.sys;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.entity.NormalEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "sys_role") // JPA-Hibernate
public class RoleEntity extends NormalEntity {

    @Schema(name = "roleName")
    private String roleName;

    @Schema(name = "roleCode")
    private String roleCode;

    @Schema(name = "remark", description = "備考")
    private String remark;

}
