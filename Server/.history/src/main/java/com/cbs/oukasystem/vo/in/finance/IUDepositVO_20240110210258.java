package com.cbs.oukasystem.vo.in.operate;

import java.util.Date;

import javax.persistence.Column;

import com.cbs.oukasystem.common.BusinessEnum.EnumStatus;
import com.cbs.oukasystem.vo.NormalVO;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUAccidentVO extends NormalVO {

    /*
     * 車両
     */

    @Schema(name = "carId", description = "車両番号")
    private String carId;

    @Schema(name = "carNo", description = "車両番号")
    private String carNo;

    @Schema(name = "carName", description = "車両名", example = "車両名")
    private String carName;

    /*
     * ドライバー
     */
    @Schema(name = "driverId", description = "車両番号")
    private String driverId;

    @Schema(name = "driverNo", description = "車両番号")
    private String driverNo;

    @Schema(name = "driverName", description = "車両名", example = "車両名")
    private String driverName;

    /*
     * 事故
     */
    @Schema(name = "accidentType")
    private String accidentType;

    @Schema(name = "accidentTypeCode")
    private String accidentTypeCode;

    @Schema(name = "details")
    private String details;

    @Schema(name = "confirmBy")
    private String confirmBy;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "accidentTime")
    private Date accidentTime;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "noticeTime")
    private Date noticeTime;

    @Schema(name = "images")
    private String images;

    /*
     * 修理
     */

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "repairTime")
    private Date repairTime;

    @Schema(name = "repairBy")
    private String repairBy;

    @Schema(name = "insuranceAmount")
    private Double insuranceAmount;

    @Schema(name = "driverAmount")
    private Double driverAmount;

    @Schema(name = "amount")
    private Double amount;

    @Schema(name = "companyAmount")
    private Double companyAmount;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "financeNoticeTime", description = "財務通知日")
    private Date financeNoticeTime;

    @Schema(name = "financeAmount")
    private Double financeAmount;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "financeTime")
    private Date financeTime;

    @Schema(name = "financeBy")
    private String financeBy;

    @Schema(name = "financeByName")
    private String financeByName;

    @Column(name = "status")
    @Schema(name = "status")
    private EnumStatus status = EnumStatus.Waiting;

    @Column(name = "remark")
    @Schema(name = "remark", description = "備考")
    private String remark;

}
