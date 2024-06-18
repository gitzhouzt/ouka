package com.cbs.oukasystem.entity.user;

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

    /*
     * ユーザー
     */
    @Schema(name = "userId", description = "userId")
    private String userId;

    @Schema(name = "userName", description = "ユーザー名", example = "ユーザー名")
    private String userName;

    @Column(name = "remark")
    @Schema(name = "remark", description = "備考")
    private String remark;

}
