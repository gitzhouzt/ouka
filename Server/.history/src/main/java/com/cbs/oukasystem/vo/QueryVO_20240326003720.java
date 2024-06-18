package com.cbs.oukasystem.vo;

import java.io.Serializable;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.cbs.oukasystem.common.Constants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class QueryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "id", description = "Id", required = false)
    private String id;

    @Schema(name = "keyword", description = "キーワード", example = "名前/メール/携帯番号など", required = false)
    private String keyword;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @Schema(name = "beginTime", description = "追加開始時間", example = "2022-01-14 11:03:07", required = false)
    private Date beginTime;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @Schema(name = "endTime", description = "追加終了時間", example = "2022-01-14 11:03:07", required = false)
    private Date endTime;

    @Schema(name = "isAudit", description = "有効審査", example = "有効true 無効true", required = false)
    private Boolean isAudit;

    @Schema(name = "page", description = "ページ番号", example = "1")
    private int page = Constants.NUM_1;

    @Schema(name = "pageSize", description = "表示件数", example = "10")
    private int pageSize = Constants.PAGE_SIZE_10;

    @Schema(name = "sort", description = "sort", example = "sort", required = false)
    private String sort;

    @Schema(name = "sortType", description = "sortType", example = "sortType", required = false)
    private String sortType;

    @Schema(name = "lang", description = "lang")
    private String lang;

}
