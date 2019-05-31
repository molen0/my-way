package adinnet.response.code;

/**
 * Created by 66 on 2017/10/27.
 */
public enum CCode implements RCode {


    //基本
    C_SAVE_FAILED(4000, "保存失败"),
    C_UPDATE_FAILED(4001, "更新失败"),
    C_DELETE_FAILED(4002, "删除失败"),
    C_Query_FAILD(4003, "查询失败"),

    //参数相关
    C_PARAM_EMPTY_OR_PARSE_EX(4010, "参数为空，请稍候重试!"),
    C_PARAM_EX(4011, "参数验证异常"),
    C_PARAM_IS_NULL(4012, "参数为空！"),
    C_PARAM_QUERY_IS_NULL(4013, "查询参数为空"),
    C_OPENID_IS_NULL(4054, "查询参数为空"),
    C_PARAM_VALIDATE_FAIL(4014, "参数验证失败"),
    C_GET_TOKEN(4053, "IP"),
    C_DOCTOR_IS_NULL(4055,"医生不存在"),
    C_DOCTOR_Auditing(4056,"实名认证后才可提交报名"),

    C_Buz_Ex(4020, "业务异常"),

    //登录注册相关  4020+
    //注册 4020~ 4040
    C_PASSWORD_EQUALLY(4030, "密码为空"),
    C_USER_NAME_OR_PASSWORD_EX(4031, "用户名或密码错误"),
    C_USER_OLD_PWD_EX(4032, "原密码错误"),
    C_USER_EXIST(4032, "用户已存在"),
    C_USER_HOGNBAO(4045, "红包不存在"),
    C_RECEIVED_HOGNBAO(4046, "红包已领取"),
    C_TIME_HOGNBAO(4046, "红包活动不在有效期内"),
    C_USER_NO_EXIST(4033, "用户不存在"),
    C_SMS_CODE_ERROR(4034, "验证码错误"),
    C_SMS_CODE_EXPIRE(4082, "验证码已经过期,请重新获取！"),
    C_2_MOBILE_NOT_EQUALLY(4035, "两次输入验证码不一样"),
    C_EMAIL_NO_EXITS(4036, "邮箱不存在！"),
    C_EMAIL_EXITS(4036, "邮箱已存在！"),
    C_EMAIL_Send_Failed(4037, "发送邮箱失败"),
    C_USER_AUTH(4037, "用户已认证过"),
    C_Mobile_Exist(4038, "手机号码已存在"),
    C_Mobile_No_Exist(4039, "手机号码不存在"),
    C_SMS_Send(4040, "短信已发送"),

    //登录 4040~4050
    C_USER_NO_LOGIN(4041, "尚未登录"),
    C_USER_DISABLED(4042, "用户被禁用"),
    C_USER_LOGIN_FAILED(4043, "登录失败"),
    C_USER_NO_PERMISSION(4044, "没有权限"),

    //忘记密码  4050~4060
    C_USER_RESET_PASSWORD_FAILED(4051, "用户重置密码失败"),
    C_USER_MODIFY_PASSWORD_FAILED(4052, "修改密码失败"),

    C_DATA_EXIST(4061, "数据已存在"),
    C_DATA_NO_EXIST(4062, "数据不存在"),

    C_Http_Bad(4071, "请求失败"),
    C_Upload_Failed(4081, "上传失败"),

    //课程相关  4060~5070
    COURSE_ALREADY_ENVALATE(4060, "您已经评价过此课程！"),
    COURSE_ALREADY_FEEDBACK(4061, "您已经反馈过此课程！"),

    //邮箱修改密码 4070~4080

    //学期 4082~4090
    C_Semester_NULL(4082, "本学期暂未开课"),
    C_Doctor_NULL(4083, "系统暂无您的信息，请联系管理员"),
    C_Semester_SignUp(4084, "本学期已报过名"),
    C_Token_OutTime(4091, "Token已失效");
    private int code;

    private String message;

    CCode(String message) {
        this.message = message;
    }

    CCode(int code, String message) {
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
