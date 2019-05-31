package com.adinnet.common.status;

/**
 * Created by wangx on 2018/9/25.
 */
public enum IsDeleted {
    _0(0),   //删除
    _1(1);   //正常

    private int code;


    IsDeleted(int code) {
        this.code = code;

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}
