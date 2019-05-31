package com.wonders.healthcloud.sample.exception;

import com.wonders.healthcloud.sample.enums.ExceptionEnum;

public class CommonException extends Exception {

    private Integer code;

    private String message;

    public CommonException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonException(ExceptionEnum exceptionEnum) {
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
