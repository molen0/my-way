package com.adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by adinnet on 2018/9/20.
 */
@Data
@Entity
@Table(name ="tb_semester_doctor")
public class SemesterDoctor extends BaseEntity implements Serializable {
    /**主键*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    /**学期id*/
    @Column(name="semester_id")
    private Integer semesterId;

    /**医生id*/
    @Column(name="doctor_id")
    private Integer doctorId;

    /**创建时间*/
    @Column(name="create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**更新时间*/
    @Column(name="update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
