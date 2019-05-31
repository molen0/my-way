package adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2018/10/31.
 */
@Data
@Entity
@Table(name ="tb_doctor")
public class ReportDoctor extends BaseEntity implements Serializable {
    /**主键*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    /**手机号*/
    @Column(name="phone")
    private String phone;
    /**医生属性*/
    @Column(name="property")
    private Integer property; //医生属性 0:乡村医生 1:全科医生'
    /**职称*/
    @Column(name="title")
    private String title;
    /**年龄*/
    @Column(name="age")
    private Integer age;
    /**区域编码*/
    @Column(name="area")
    private String area;
    /**区域名*/
    @Transient
    private String areaName;
    /**姓名*/
    @Column(name="name")
    private String name;
    /**性别*/
    @Column(name="gender")
    private String gender;
    /**单位*/
    @Column(name="company")
    private String company;
    /**学分*/
    @Transient
    private String credit;
    /**邮箱*/
    @Column(name="email")
    private String email;
    /**学期id*/
    @Transient
    private Integer sdId;
    /**学期名*/
    @Transient
    private String sName;

    /**创建时间*/
    @Column(name="create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**修改时间*/
    @Column(name="update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
