package com.adinnet.repository;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author wangren
 * @Description: 测试实体
 * @create 2018-09-13 14:07
 **/
@Entity
@Data
@Table(name = "admin")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class UserInfo extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="uid")
    private Integer uid;
    @Column(name="username",unique =true)
    private String username;//帐号
    @Column(name="password")
    private String password; //密码;
    @Column(name="salt",updatable=false)
    private String salt;//加密密码的盐
    @Column(name="state")
    private Integer state;//用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 1:正常状态,2：用户被锁定.
    @Column(name="updated_at")
    private String updatedAt;
    @Column(name="created_at",updatable=false)
    private String createdAt;
    @Column(name="is_system",updatable=false)
    private Integer isSystem;
    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name = "admin_id")
    private List<SysUserRole> sysRoleList;

    @Transient
    private Integer[] roleId;

    /**
     * 密码盐.
     * @return
     */
    public String getCredentialsSalt(){
        return this.username+this.salt;
    }
}
