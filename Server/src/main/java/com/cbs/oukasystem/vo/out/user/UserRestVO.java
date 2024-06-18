package com.cbs.oukasystem.vo.out.user;

import java.util.Date;

import javax.persistence.Column;

import com.cbs.oukasystem.vo.OperatorVO;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(name = "UserRestVO")
@NoArgsConstructor
@AllArgsConstructor
public class UserRestVO extends OperatorVO {

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

    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+9")
    @Schema(name = "startTime")
    private Date startTime;

    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+9")
    @Schema(name = "endTime")
    private Date endTime;

    @Column(name = "remark")
    @Schema(name = "remark", description = "備考")
    private String remark;

}
