package com.adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 访问在线考试日志
 * Created by RuanXiang on 2018/11/20.
 */
@Data
@Entity
@Table(name ="tb_doctor_visit_log")
public class DoctorVisitLog {
    /**主键*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    /**用户id*/
    @Column(name="uid")
    private String uid;

    /**姓名*/
    @Column(name="name")
    private String name;

    /**创建时间*/
    @Column(name="create_time",updatable=false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
