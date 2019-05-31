package com.adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 学期
 * Created by RuanXiang on 2018/9/18.
 */
@Data
@Entity
@Table(name ="tb_semester")
public class Semester extends BaseEntity implements Serializable {
    /**主键*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    /**学期名称*/
    @Column(name="name")
    private String name;

    /**状态 0：停用  1：正常*/
    @Column(name="status")
    private Integer status;

    /**创建人id*/
    @Column(name="user_id")
    private Integer userId;

    /**学期每道题分钟数*/
    @Column(name="minutes")
    private Integer minutes;

    /**成绩公布时间*/
    @Column(name="time")
    private String time;

    /**考试结束时间*/
    @Column(name="course_end_time")
    private String courseEndTime;

    /**电子手册地址*/
    @Column(name="hand_book_url")
    private String handBookUrl;

    /**0:删除 1：正常*/
    @Column(name="is_delete")
    private Integer isDelete;

    /**课程数*/
    @Transient
    private Integer courseCount;

    /**创建时间*/
    @Column(name="create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**更新时间*/
    @Column(name="update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
