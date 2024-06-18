package com.cbs.oukasystem.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class OperatorVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "Id", description = "id", example = "0f525d1a-6509-0512-2988-3a01687fd77f")
    private String id;

    @Schema(name = "createBy", description = "追加者", example = "0f525d1a-6509-0512-2988-3a01687fd77f")
    private String createBy;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "createTime", description = "追加時間", example = "2022-01-14 11:03:07")
    private Date createTime;

    @Schema(name = "updateBy", description = "編集者", example = "0f525d1a-6509-0512-2988-3a01687fd77f")
    private String updateBy;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "updateTime", description = "編集時間", example = "2022-01-14 11:03:07")
    private Date updateTime;

    @Schema(name = "isAudit", description = "有効審査", example = "有効true 無効true")
    private Boolean isAudit;

    @Schema(name = "auditBy", description = "審査者", example = "0f525d1a-6509-0512-2988-3a01687fd77f")
    private String auditBy;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "auditTime", description = "審査時間", example = "2022-01-14 11:03:07")
    private Date auditTime;

    @Schema(name = "isDelete", description = "削除審査", example = "削除true 未削除true")
    private Boolean isDelete;

    @Schema(name = "deleteBy", description = "削除者", example = "0f525d1a-6509-0512-2988-3a01687fd77f")
    private String deleteBy;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "deleteTime", description = "削除時間", example = "2022-01-14 11:03:07")
    private Date deleteTime;

    @Schema(name = "lang", description = "lang")
    private String lang;
}
