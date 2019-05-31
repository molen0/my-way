package adinnet.common.status;

/**
 * Created by 66 on 2017/5/23.
 */
public enum IsJudge {
    NO(0),
    YES(1);

    private Integer code;


    IsJudge(Integer code) {
        this.code = code;

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }


    public static boolean validate(int type) {
        if (type == NO.getCode() || type == YES.getCode()) {
            return true;
        }
        return false;
    }


}
