package com.cbs.oukasystem.entity.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.entity.OperatorEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "sys_menu") // JPA-Hibernate
public class MenuEntity extends OperatorEntity {

    @Column(length = 50, name = "menu_ame")
    @Schema(name = "name", description = "メニュー name")
    private String name;

    @Column(length = 50, name = "menu_title")
    @Schema(name = "title", description = "メニュー title")
    private String title;

    @Column(length = 20, name = "menu_icon")
    @Schema(name = "icon", description = "メニューアイコン")
    private String icon;

    @Column(length = 20, name = "menu_component")
    @Schema(name = "component", description = "コンポーネント")
    private String component;

    @Column(length = 50)
    @Schema(name = "parentID", description = "上層メニューID")
    private String parentId;

    @Column(name = "menu_requires_auth")
    @Schema(name = "requiresAuth", description = "requiresAuth")
    private Boolean requiresAuth;

    @Column(length = 50, name = "menu_path")
    @Schema(name = "path", description = "path")
    private String path;

}