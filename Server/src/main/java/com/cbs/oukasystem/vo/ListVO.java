package com.cbs.oukasystem.vo;

import java.util.List;

import com.cbs.oukasystem.common.Constants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema
@AllArgsConstructor
@NoArgsConstructor
public class ListVO<T> {

    @Schema(name = "page", description = "ページ番号")
    private int page = 0;

    @Schema(name = "pageSize", description = "表示件数")
    private int pageSize = Constants.PAGE_SIZE_10;

    @Schema(name = "pageCount", description = "総ページ数")
    private int pageCount = 0;

    @Schema(name = "itemCount", description = "検索件数")
    private long itemCount;

    @Schema(name = "list", description = "検索リスト")
    private List<T> list;

}