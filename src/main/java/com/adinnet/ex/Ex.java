package com.adinnet.ex;


import com.adinnet.response.code.RCode;

@SuppressWarnings("serial")
public final class Ex extends RuntimeException {

    private String message;
    private Integer code;

    public Ex(String message) {
        this.message = message;
        this.code = -1;
    }

    public Ex(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public Ex(RCode code) {
        this.code=code.getCode();
        this.message=code.getMessage();
    }


    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
