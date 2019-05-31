package com.adinnet.ex;


import com.adinnet.response.code.RCode;

/**
 * Created by 66 on 2017/4/21.
 */
public class BuzEx extends RuntimeException {
    private RCode code;

    public BuzEx(RCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public BuzEx(RCode code, String message) {
        code.setMessage(message);
        this.code = code;

    }

    public static BuzEx build(RCode code) {
        return new BuzEx(code);
    }

    public static BuzEx build(RCode code, String message) {
        return new BuzEx(code, message);
    }


    public RCode getCode() {
        return code;
    }

    public void setCode(RCode code) {
        this.code = code;
    }
}
