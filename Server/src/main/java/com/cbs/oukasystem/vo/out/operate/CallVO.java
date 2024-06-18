package com.cbs.oukasystem.vo.out.operate;

import java.util.Date;

import com.cbs.oukasystem.vo.OperatorVO;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "CallVO") // swagger3
@Data // lombok
@AllArgsConstructor
@NoArgsConstructor
public class CallVO extends OperatorVO {

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
     * 点呼内容
     */

    @Schema(name = "date", description = "点呼日", example = "点呼日")
    private String date;

    @Schema(name = "amCallMethod", description = "点呼方法", example = "点呼方法")
    private String amCallMethod;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "amCallTime", description = "点呼時間", example = "点呼時間")
    private Date amCallTime;

    @Schema(name = "amConfirmBy", description = "確認者", example = "確認者")
    private String amConfirmBy;

    @Schema(name = "amRemark", description = "備考")
    private String amRemark;

    @Schema(name = "am1", description = "アルコール検知器", example = "アルコール検知器")
    private String am1;

    @Schema(name = "am2", description = "酒気帯びの有無", example = "酒気帯びの有無")
    private String am2;

    @Schema(name = "am3", description = "異常の有無", example = "異常の有無")
    private String am3;

    @Schema(name = "am4", description = "睡眠不足有無", example = "睡眠不足有無")
    private String am4;

    @Schema(name = "amImage", description = "点呼写真")
    private String amImage;

    @Schema(name = "pmCallMethod", description = "点呼方法", example = "点呼方法")
    private String pmCallMethod;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+9")
    @Schema(name = "pmCallTime", description = "点呼時間", example = "点呼時間")
    private Date pmCallTime;

    @Schema(name = "pmConfirmBy", description = "確認者", example = "確認者")
    private String pmConfirmBy;

    @Schema(name = "pmRemark", description = "備考")
    private String pmRemark;

    @Schema(name = "pm1", description = "アルコール検知器", example = "アルコール検知器")
    private String pm1;

    @Schema(name = "pm2", description = "酒気帯びの有無", example = "酒気帯びの有無")
    private String pm2;

    @Schema(name = "pm3", description = "異常の有無", example = "異常の有無")
    private String pm3;

    @Schema(name = "pm4", description = "睡眠不足有無", example = "睡眠不足有無")
    private String pm4;

    @Schema(name = "pmImage", description = "点呼写真")
    private String pmImage;

    @Schema(name = "weather", description = "天気", example = "天気")
    private String weather;

    @Schema(name = "manager", description = "運行管理者", example = "運行管理者")
    private String manager;

    @Schema(name = "helper", description = "補佐", example = "補佐")
    private String helper;

}
