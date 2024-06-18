package com.cbs.oukasystem.entity;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema // swagger3
@Data // lombok
@MappedSuperclass
public class NormalEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "system_uuid")
    @GenericGenerator(name = "system_uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Schema(name = "Id", description = "uuid", example = "0f525d1a-6509-0512-2988-3a01687fd77f")
    private String id;
}
