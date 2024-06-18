package com.cbs.oukasystem.vo.in.user;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class IUUserLogVO extends NormalVO {

    /*
     * ドライバー
     */
    @Schema(name = "driver1Id", description = "userId")
    private String driver1Id;

    @Schema(name = "driver1No", description = "ドライバー番号", example = "ドライバー番号")
    private String driver1No;

    @Schema(name = "driver1Name", description = "ドライバー名", example = "ドライバー名")
    private String driver1Name;

    @Schema(name = "driver2Id", description = "userId")
    private String driver2Id;

    @Schema(name = "driver2No", description = "ドライバー番号", example = "ドライバー番号")
    private String driver2No;

    @Schema(name = "driver2Name", description = "ドライバー名", example = "ドライバー名")
    private String driver2Name;

    /*
     * 車両
     */
    @Schema(name = "carId", description = "carId")
    private String carId;

    @Schema(name = "carNo", description = "車両番号", example = "車両番号")
    private String carNo;

    @Schema(name = "carName", description = "車両", example = "車両")
    private String carName;

    /*
     * 
     */
    @Schema(name = "date", description = "日期", example = "日期")
    private String date;

    @Schema(name = "startDistance", description = "開始前距離", example = "開始前距離")
    private Double startDistance;

    @Schema(name = "endDistance", description = "終了距離", example = "終了距離")
    private Double endDistance;

    @Schema(name = "diffDistance", description = "差引距離", example = "差引距離")
    private Double diffDistance;

    @Schema(name = "jisshaDistance", description = "実車距離", example = "実車距離")
    private Double jisshaDistance;

    @Schema(name = "kaisouDistance", description = "回送距離", example = "回送距離")
    private Double kaisouDistance;

    @Schema(name = "refueling", description = "給油量", example = "給油量")
    private Double refueling;

    @Schema(name = "startAddress", description = "開始場所", example = "開始場所")
    private String startAddress;

    @Schema(name = "endAddress", description = "終了場所", example = "開始場所")
    private String endAddress;

    @Schema(name = "manager", description = "運行管理者", example = "運行管理者")
    private String manager;

    @Schema(name = "remark", description = "報告及び特記事項", example = "報告及び特記事項")
    private String remark;

    @Schema(name = "startBy", description = "開始場所確認者", example = "開始場所確認者")
    private String startBy;

    @Schema(name = "endBy", description = "終了場所確認者", example = "終了場所確認者")
    private String endBy;

    @Schema(name = "personNum", description = "人数", example = "人数")
    private int personNum;

    @Schema(name = "weather", description = "天気", example = "天気")
    private String weather;

    @Schema(name = "images", description = "写真", example = "写真")
    private String images;

    /**
     * 運転席点検
     */

    @Schema(name = "a1", description = "ブレーキぺルタ", example = "踏みしろ・きき具合")
    private String a1;

    @Schema(name = "a2", description = "駐車ブレーキ・レバー", example = "引きしろ")
    private String a2;

    @Schema(name = "a3", description = "エンジン", example = "かかり具合・低速、加速状態")
    private String a3;

    @Schema(name = "a4", description = "ウインド・ウォッシャー", example = "噴射状態")
    private String a4;

    @Schema(name = "a5", description = "ウイパー", example = "払拭状態")
    private String a5;

    @Schema(name = "a6", description = "室内", example = "清掃状態")
    private String a6;

    @Schema(name = "a7", description = "ドアロック・座席ベルト", example = "損傷有無")
    private String a7;

    /**
     * ルーム点検・エンジン
     */

    @Schema(name = "b1", description = "ウィンド・ウォッシャー", example = "液量")
    private String b1;

    @Schema(name = "b2", description = "ブルーキリザーブ・タンク", example = "液量")
    private String b2;

    @Schema(name = "b3", description = "バッテリー", example = "液量")
    private String b3;

    @Schema(name = "b4", description = "ラジエーター", example = "水量")
    private String b4;

    @Schema(name = "b5", description = "エンジンオイル", example = "油量")
    private String b5;

    @Schema(name = "b6", description = "ファンベルト", example = "張り具合・損傷")
    private String b6;

    /**
     * 車の周りの点検
     */

    @Schema(name = "c1", description = "タイヤ", example = "空気圧")
    private String c1;

    @Schema(name = "c2", description = "タイヤ", example = "亀裂、損傷")
    private String c2;

    @Schema(name = "c3", description = "タイヤ", example = "異常な摩耗")
    private String c3;

    @Schema(name = "c4", description = "タイヤ", example = "溝の深さ")
    private String c4;

    @Schema(name = "c5", description = "灯火装置及び方向指示器", example = "点灯具合")
    private String c5;

    @Schema(name = "c6", description = "灯火装置及び方向指示器", example = "点滅具合")
    private String c6;

    @Schema(name = "c7", description = "灯火装置及び方向指示器", example = "汚れ、損傷")
    private String c7;

    @Schema(name = "c8", description = "ディスク・ホイールの取付", example = "取り付け状況")
    private String c8;

    /**
     * その他
     */

    @Schema(name = "d1", description = "非常信号計・工具", example = "備え付け")
    private String d1;

    @Schema(name = "d2", description = "車検証・保険証", example = "備え付け")
    private String d2;

    @Schema(name = "checkRemark", description = "点検備考")
    private String checkRemark;
}
