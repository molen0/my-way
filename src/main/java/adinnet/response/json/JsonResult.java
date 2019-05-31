package adinnet.response.json;

import com.adinnet.response.code.CRCode;
import com.adinnet.response.code.RCode;

public class JsonResult {
    private int code;

    private Object result;

    private String message;

    private String token;

    public JsonResult() {
    }

    public JsonResult(int code, Object result) {
        this.code = code;
        this.result = result;

    }

    public JsonResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public JsonResult(int code, Object result, String message) {
        this.code = code;
        this.result = result;
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static JsonResult build(RCode ex) {
        return new JsonResult(ex.getCode(), ex.getMessage());
    }


    public static JsonResult OK(Object result) {
        RCode code = CRCode.C_OK;
        return new JsonResult(code.getCode(), result, code.getMessage());
    }

    public static JsonResult OK() {
        RCode code = CRCode.C_OK;
        return new JsonResult(code.getCode(), null, code.getMessage());
    }

    public static JsonResult Fail() {
        RCode code = CRCode.C_Buz_Ex;
        return new JsonResult(code.getCode(), null, code.getMessage());
    }

    public static JsonResult build(Integer code, String message) {
        return new JsonResult(code, message);
    }


}
