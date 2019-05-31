package com.wonders.healthcloud.sample.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionMessage {

    private Integer code;

    private String msg;

    public ExceptionMessage(CommonException ex) {
        code = ex.getCode();
        msg = ex.getMessage();
    }

    public ExceptionMessage(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
