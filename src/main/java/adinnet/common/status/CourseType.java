package adinnet.common.status;

/**
 * 课程类型：1，必学课程；2，知识学习；3，经验分享
 * Created by hasee on 2018/10/10.
 */
public enum CourseType {
    MUST(1, "必学课程"),
    LEARNING(2, "知识学习"),
    EXPERIENCE(3, "经验分享");


    private int code;

    private String name;

    CourseType(int code, String name) {
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
