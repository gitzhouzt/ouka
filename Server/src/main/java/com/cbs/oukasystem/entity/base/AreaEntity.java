package com.cbs.oukasystem.entity.base;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.entity.NormalEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "base_area") // JPA-Hibernate
public class AreaEntity extends NormalEntity {

    @Schema(name = "areaId")
    private String areaId;

    @Schema(name = "areaCode")
    private String areaCode;

    @Schema(name = "areaName")
    private String areaName;

    @Schema(name = "cityCode")
    private String cityCode;

    @Schema(name = "cityName")
    private String cityName;

}
