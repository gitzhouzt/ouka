package com.cbs.oukasystem.entity.user;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.cbs.oukasystem.entity.NormalEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "user_train") // JPA-Hibernate
public class UserTrainEntity extends NormalEntity {

    @Column(name = "user_id", insertable = false, updatable = false, nullable = true)
    @Schema(name = "userId", description = "Id", example = "userId")
    private String userId;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id")
    @Schema(name = "user", description = "user", example = "user")
    private UserEntity user = new UserEntity();

    @Schema(name = "trainYear", description = "培训年份")
    private String trainYear;

    @Schema(name = "trainDate1", description = "第一次培训日期")
    private Date trainDate1;

    @Schema(name = "trainDate2", description = "第二次培训日期")
    private Date trainDate2;

    @Schema(name = "trainDate3", description = "第三次培训日期")
    private Date trainDate3;

    @Schema(name = "trainDate4", description = "第四次培训日期")
    private Date trainDate4;
}
