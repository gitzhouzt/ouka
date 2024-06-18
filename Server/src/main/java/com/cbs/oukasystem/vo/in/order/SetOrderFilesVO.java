package com.cbs.oukasystem.vo.in.order;

import com.cbs.oukasystem.vo.NormalVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class SetOrderFilesVO extends NormalVO {

    @Schema(name = "files", description = "附件")
    private String files;

}
