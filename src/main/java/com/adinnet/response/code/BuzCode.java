package com.adinnet.response.code;

/**
 * Created by RuanXiang on 2018/2/23.
 */
public enum BuzCode implements RCode {

    ItemApply_Add(2001, "项目申请已提交过"),
    version_exits(2002, "版本号已存在"),
    Item_No(2003,"该产品不存在"),
    ItemType_No(2004,"该产品类型不存在"),
    UserAuth_Has(2005,"该用户已提交过认证"),
    CLIENT_ERROR(2034,"client_id 或 client_secret 错误"),
    M_PARAM_EMPTY_OR_PARSE_EX(30004, "参数解析有误，请稍候重试！"),
    M_BUZ_EX(30001, "系统繁忙，请稍候重试！"),
    business_no_exist(40001,"业务机会不存在")
    ;
    private int code;
    private String message;
    BuzCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
