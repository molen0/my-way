package adinnet.response.code;

/**
 * Created by 66 on 2017/10/27.
 * common response code
 */
public enum CRCode implements RCode {
    /**
     * ***************正常处理状态 2000*******************
     */
    C_OK(2000, "OK"),

    /**
     * ************3000+ 通用参数**********************
     */
    C_SYS_EX(3000, "系统繁忙"),

    NO_PERMISSION(3001, "没有权限"),

    C_DIGITAL_SIGN_EX(3002, "参数签名异常"),

    C_TOKEN_EX(3003, "token 异常"),

    C_Buz_Ex(3004, "业务异常");

    private int code;

    private String message;


    CRCode(String message) {
        this.message = message;
    }

    CRCode(int code, String message) {
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
