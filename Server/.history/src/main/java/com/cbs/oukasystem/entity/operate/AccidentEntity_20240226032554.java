package com.cbs.oukasystem.entity.operate;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cbs.oukasystem.common.EnumStatus;
import com.cbs.oukasystem.entity.OperatorEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema // swagger3
@Data // lombok
@Entity // JPA-Hibernate
@Table(name = "operate_accident") // JPA-Hibernate
@NoArgsConstructor
@AllArgsConstructor
public class AccidentEntity extends OperatorEntity {

    /*
     * 車両
     */

    @Column(name = "carId")
    @Schema(name = "carId", description = "車両番号")
    private String carId;

    @Column(name = "carNo")
    @Schema(name = "carNo", description = "車両番号")
    private String carNo;

    @Column(name = "carName")
    @Schema(name = "carName", description = "車両名", example = "車両名")
    private String carName;

    /*
     * ドライバー
     */
    @Column(name = "driverId")
    @Schema(name = "driverId", description = "車両番号")
    private String driverId;

    @Column(name = "driverNo")
    @Schema(name = "driverNo", description = "車両番号")
    private String driverNo;

    @Column(name = "driverName")
    @Schema(name = "driverName", description = "車両名", example = "車両名")
    private String driverName;

    /*
     * 事故
     */
    @Schema(name = "accidentType")
    private String accidentType;

    @Schema(name = "accidentTypeCode")
    private String accidentTypeCode;

    @Column(length = 1000)
    @Schema(name = "details")
    private String details;

    @Schema(name = "confirmBy")
    private String confirmBy;

    @Schema(name = "accidentTime")
    private Date accidentTime;

    @Schema(name = "noticeTime", description = "事故通知日")
    private Date noticeTime;

    @Column(length = 1000)
    @Schema(name = "images")
    private String images;

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

    @Schema(name = "amount")
    private Double amount;

    @Schema(name = "insuranceAmount")
    private Double insuranceAmount;

    @Schema(name = "driverAmount")
    private Double driverAmount;

    @Schema(name = "companyAmount")
    private Double companyAmount;

    @Schema(name = "financeAmount")
    private Double financeAmount;

    @Schema(name = "financeNoticeTime", description = "財務通知日")
    private Date financeNoticeTime;

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
