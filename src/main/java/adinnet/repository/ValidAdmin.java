package adinnet.repository;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author wangren
 * @Description: 登录验证实体
 * @create 2018-09-19 14:39
 **/
@Data
public class ValidAdmin {
    @NotEmpty(message="用户名不能为空")
    private String username;

    @NotEmpty(message="密码不能为空")
    private String password;
}
