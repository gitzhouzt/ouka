package com.cbs.oukasystem.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.cbs.oukasystem.common.LoginUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema // swagger3
@Data // lombok
@MappedSuperclass // JPA-Hibernate
public class OperatorEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "system_uuid")
    @GenericGenerator(name = "system_uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Schema(name = "Id", description = "id", example = "0f525d1a-6509-0512-2988-3a01687fd77f")
    private String id;

    @Schema(name = "isAudit", description = "有効審査", example = "有効true 無効false")
    private Boolean isAudit;

    @Schema(name = "auditBy", description = "審査者", example = "0f525d1a-6509-0512-2988-3a01687fd77f")
    private String auditBy;

    @Schema(name = "auditTime", description = "審査時間", example = "2022-01-14 11:03:07")
    private Date auditTime;

    @Schema(name = "createBy", description = "追加者", example = "0f525d1a-6509-0512-2988-3a01687fd77f")
    private String createBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Schema(name = "createTime", description = "追加時間", example = "2022-01-14 11:03:07")
    private Date createTime;

    @Schema(name = "updateBy", description = "編集者", example = "0f525d1a-6509-0512-2988-3a01687fd77f")
    private String updateBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Schema(name = "updateTime", description = "編集時間", example = "2022-01-14 11:03:07")
    private Date updateTime;

    @Schema(name = "isDelete", description = "削除審査", example = "削除true 未削除false")
    private Boolean isDelete;

    @Schema(name = "deleteBy", description = "削除者", example = "0f525d1a-6509-0512-2988-3a01687fd77f")
    private String deleteBy;

    @Schema(name = "deleteTime", description = "削除時間", example = "2022-01-14 11:03:07")
    private Date deleteTime;

    @Schema(name = "sort")
    private int sort = 0;

    @PrePersist
    public void prePersist() {
        Date now = new Date();
        if (createTime == null) {
            createTime = now;
            createBy = LoginUtils.getLoginUserId();
        }
        if (updateTime == null) {
            updateTime = now;
            updateBy = LoginUtils.getLoginUserId();
        }
        if (isAudit == null) {
            isAudit = true;
            auditBy = LoginUtils.getLoginUserId();
            auditTime = now;
        }
        if (isDelete == null) {
            isDelete = false;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updateBy = LoginUtils.getLoginUserId();
        updateTime = new Date();
    }
}
