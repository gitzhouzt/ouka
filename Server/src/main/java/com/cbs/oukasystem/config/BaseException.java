package com.cbs.oukasystem.config;

import lombok.Data;

@Data
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    protected Integer errorCode = -1;

    protected String errorMsg = "";

    protected String localeKey = "";

    protected String errorDetails = "";

    public BaseException() {
        super();
    }

    public BaseException(BaseErrorInfoInterface errorInfoInterface) {
        super(errorInfoInterface.getErrorMsg());
        this.errorCode = errorInfoInterface.getErrorCode();
        this.errorMsg = errorInfoInterface.getErrorMsg();
        this.localeKey = errorInfoInterface.getLocaleKey();
    }

    public BaseException(BaseErrorInfoInterface errorInfoInterface, String errorDetails) {
        super(errorInfoInterface.getErrorMsg());
        this.errorCode = errorInfoInterface.getErrorCode();
        this.errorMsg = errorInfoInterface.getErrorMsg();
        this.localeKey = errorInfoInterface.getLocaleKey();
        this.errorDetails = errorDetails;
    }

    public BaseException(BaseErrorInfoInterface errorInfoInterface, Throwable cause) {
        super(errorInfoInterface.getErrorMsg(), cause);
        this.errorCode = errorInfoInterface.getErrorCode();
        this.errorMsg = errorInfoInterface.getErrorMsg();
        this.localeKey = errorInfoInterface.getLocaleKey();
    }

    public BaseException(BaseErrorInfoInterface errorInfoInterface, Throwable cause, String errorDetails) {
        super(errorInfoInterface.getErrorMsg(), cause);
        this.errorCode = errorInfoInterface.getErrorCode();
        this.errorMsg = errorInfoInterface.getErrorMsg();
        this.localeKey = errorInfoInterface.getLocaleKey();
        this.errorDetails = errorDetails;
    }

    public BaseException(String errorMsg, String localeKey) {
        super(errorMsg);
        this.errorMsg = errorMsg;
        this.localeKey = localeKey;
    }

    public BaseException(int errorCode, String errorMsg, String localeKey) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.localeKey = localeKey;
    }

    public BaseException(String errorMsg, String localeKey, String errorDetails) {
        super(errorMsg);
        this.errorMsg = errorMsg;
        this.localeKey = localeKey;
        this.errorDetails = errorDetails;
    }

    public BaseException(int errorCode, String errorMsg, String localeKey, String errorDetails) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.localeKey = localeKey;
        this.errorDetails = errorDetails;
    }

    public BaseException(int errorCode, String errorMsg, String localeKey, String errorDetails, Throwable cause) {
        super(errorMsg, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.localeKey = localeKey;
        this.errorDetails = errorDetails;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
