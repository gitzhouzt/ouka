package com.cbs.oukasystem.vo.out.operate;

import java.util.Date;

import javax.persistence.Column;

import com.cbs.oukasystem.common.CommonEnum.EnumStatus;
import com.cbs.oukasystem.vo.OperatorVO;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "AccidentVO") // swagger3
@Data // lombok
@AllArgsConstructor
@NoArgsConstructor
public class AccidentVO extends OperatorVO {

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

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "repairTime")
    private Date repairTime;

    @Schema(name = "repairBy")
    private String repairBy;

    @Schema(name = "proportion", description = "责任比例")
    private String proportion;

    @Schema(name = "responsible", description = "主责任方")
    private String responsible;

    /*
     * 精算
     */

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
    private EnumStatus status = EnumStatus.Filling;

    @Column(name = "remark")
    @Schema(name = "remark", description = "備考")
    private String remark;

}
