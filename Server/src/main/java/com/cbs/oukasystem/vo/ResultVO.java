package com.cbs.oukasystem.vo;

import java.io.Serializable;

import com.cbs.oukasystem.common.MessageEnum.EnumResponseCheck;
import com.cbs.oukasystem.config.BaseErrorInfoInterface;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "code", description = "ステータス", example = "200")
    private Integer code = EnumResponseCheck.SUCCESS.getErrorCode();

    @Schema(name = "message", description = "エラーメッセージ", example = "ok")
    private String message = EnumResponseCheck.SUCCESS.getErrorMsg();

    @Schema(name = "data", description = "返却値")
    private T data = null;

    public ResultVO() {
    }

    public ResultVO(BaseErrorInfoInterface errorInfo) {
        this.code = errorInfo.getErrorCode();
        this.message = errorInfo.getErrorMsg();
    }

    public void parserEnum(EnumResponseCheck enumCode) {
        this.code = enumCode.getErrorCode();
        this.message = enumCode.getErrorMsg();
    }

    public static <T> ResultVO<T> success(T data) {
        ResultVO<T> resultVO = new ResultVO<>();
        resultVO.parserEnum(EnumResponseCheck.SUCCESS);
        resultVO.setData(data);
        return resultVO;
    }

    public static <T> ResultVO<T> success(Integer code, String message, T data) {
        ResultVO<T> resultVO = new ResultVO<>();
        resultVO.setCode(code);
        resultVO.setMessage(message);
        resultVO.setData(data);
        return resultVO;
    }

    public static <T> ResultVO<T> error(EnumResponseCheck enumCode, T data) {
        ResultVO<T> resultVO = new ResultVO<>();
        resultVO.parserEnum(enumCode);
        resultVO.setData(data);
        return resultVO;
    }

    public static <T> ResultVO<T> error(Integer code, String message, T data) {
        ResultVO<T> resultVO = new ResultVO<>();
        resultVO.setCode(code);
        resultVO.setMessage(message);
        resultVO.setData(data);
        return resultVO;
    }
}