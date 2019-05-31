package com.adinnet.response.code;

/**
 * Created by datang on 2018-02-26.
 *  Manager response code
 */
public enum ManagerCode implements RCode{
    user_already_exist(5000,"用户已经存在"),
    mobile_is_empty(5001,"手机号为空"),
    ;
    private int code;
    private String message;

    ManagerCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
