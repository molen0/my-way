package adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 医生
 * Created by RuanXiang on 2018/9/18.
 */
@Data
@Entity
@Table(name ="tb_doctor")
public class Doctor extends BaseEntity implements Serializable {

    /**主键*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    /**姓名*/
    @Column(name="name")
    private String name;

    /**手机号*/
    @Column(name="phone")
    private String phone;

    /**性别*/
    @Column(name="gender")
    private String gender;

    /**年龄*/
    @Column(name="age")
    private Integer age;

    /**职称*/
    @Column(name="title")
    private String title;

    /**医生属性*/
    @Column(name="property")
    private Integer property;

    /**单位*/
    @Column(name="company")
    private String company;

    /**邮箱*/
    @Column(name="email")
    private String email;

    /**在万达信息系统内id*/
    @Column(name="uid")
    private String uid;

    /**头像*/
    @Column(name="photo")
    private String photo;

    /**区域编码*/
    @Column(name="area")
    private String area;

    /**
     * 实名认证 1：是 0：否
     * */
    @Transient
    private Integer auditingStatus;

    /**创建时间*/
    @Column(name="create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**修改时间*/
    @Column(name="update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**区域名称*/
    @Transient
    private String areaName;
}
