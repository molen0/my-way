package adinnet.common.status;

/**
 * Created by hasee on 2018/9/19.
 */
public enum  AttchType {
    _1(1, "课程反馈"),
    _2(2, "课程课件"),
    _3(3, "课程学习资源");

    private int code;
    private String name;

    AttchType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
