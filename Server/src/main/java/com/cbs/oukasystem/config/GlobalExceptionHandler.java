package com.cbs.oukasystem.config;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cbs.oukasystem.common.MessageEnum.EnumResponseCheck;
import com.cbs.oukasystem.service.common.I18nService;
import com.cbs.oukasystem.vo.ResultVO;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    I18nService i18nService;

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public ResultVO<Boolean> baseExceptionHandler(HttpServletRequest req, BaseException e) {
        logger.error("サービス異常:", e.getErrorMsg(), e.getErrorDetails());
        String message = i18nService.getMessage(e.getLocaleKey());
        return ResultVO.error(e.getErrorCode(), message, false);
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class, BindException.class })
    @ResponseBody
    public ResultVO<Boolean> handleMethodArgumentNotValidException(Exception exception) {
        StringBuilder errorInfo = new StringBuilder();
        BindingResult bindingResult = null;
        if (exception instanceof MethodArgumentNotValidException) {
            bindingResult = ((MethodArgumentNotValidException) exception).getBindingResult();
        }
        if (exception instanceof BindException) {
            bindingResult = ((BindException) exception).getBindingResult();
        }
        for (int i = 0; i < bindingResult.getFieldErrors().size(); i++) {
            if (i > 0) {
                errorInfo.append(",");
            }
            FieldError fieldError = bindingResult.getFieldErrors().get(i);
            errorInfo.append(fieldError.getField()).append(" :").append(fieldError.getDefaultMessage());
        }
        logger.error(errorInfo.toString());
        return ResultVO.error(-1, errorInfo.toString(), false);
    }

    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResultVO<Boolean> exceptionHandler(HttpServletRequest req, NullPointerException e) {
        logger.error("ヌルポインタ異常:", e);
        String message = i18nService.getMessage(EnumResponseCheck.SYSTEM_ERROR.getLocaleKey());
        return ResultVO.error(EnumResponseCheck.SYSTEM_ERROR.getErrorCode(), message, false);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultVO<Boolean> exceptionHandler(HttpServletRequest req, Exception e) {
        logger.error("不明な異常:", e);
        String message = i18nService.getMessage(EnumResponseCheck.SYSTEM_ERROR.getLocaleKey());
        return ResultVO.error(EnumResponseCheck.SYSTEM_ERROR.getErrorCode(), message, false);
    }
}