package com.adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 医生信息接口对象
 * Created by RuanXiang on 2018/11/13.
 */
@Data
@Entity
@Table(name ="tb_doctor_info_log")
public class DoctorInfoLog extends BaseEntity implements Serializable {
    /**主键*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    /**接口地址*/
    @Column(name="url")
    private String url;

    /**用户id*/
    @Column(name="uid")
    private String uid;

    /**token*/
    @Column(name="token")
    private String token;

    /**出参jsonstring*/
    @Column(name="outs")
    private String outs;

    /**医生id  不为空代表是已报名，更新信息*/
    @Column(name="doctor_id")
    private Integer doctorId;

    /**创建时间*/
    @Column(name="create_time",updatable=false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
