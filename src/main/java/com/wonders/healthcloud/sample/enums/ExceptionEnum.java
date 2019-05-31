package com.wonders.healthcloud.sample.enums;

public enum ExceptionEnum {
    NODATA(1000, "nodata"),
    NULL(1000, "null");

    private Integer code;

    String message;

    private ExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
