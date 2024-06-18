package com.cbs.oukasystem.entity.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.cbs.oukasystem.entity.OperatorEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "user_holiday") // JPA-Hibernate
public class UserRestEntity extends OperatorEntity {

    @Schema(name = "userId", description = "userId")
    private String userId;

    @Schema(name = "userNo")
    private String userNo;

    @Schema(name = "userName")
    private String userName;

    @Schema(name = "userRoles")
    private String userRoles;

    @Schema(name = "restType")
    private String restType;

    @Schema(name = "restTypeCode")
    private String restTypeCode;

    @Schema(name = "startTime")
    private Date startTime;

    @Schema(name = "endTime")
    private Date endTime;

    @Column(name = "remark")
    @Schema(name = "remark", description = "備考")
    private String remark;

}
