package com.cbs.oukasystem.entity.sys;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.entity.NormalEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "sys_permission") // JPA-Hibernate
public class PermissionEntity extends NormalEntity {

    @Schema(name = "roleId")
    private String roleId;

    @Schema(name = "menuId")
    private String menuId;

    @Schema(name = "actions")
    private String actions;

    @Schema(name = "remark", description = "備考")
    private String remark;

}
