package com.adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**课程反馈
 * Created by hasee on 2018/9/18.
 */
@Data
@Entity
@Table(name ="tb_course_feedback")
public class CourseFeedback extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id; // 编号

    @Column(name="course_type")
    private Integer courseType; //课程类型 ：1，必学课程；2，知识学习；3，经验分享

    @Column(name="course_id")
    private Integer courseId; //课程ID

    @Column(name="outline_id")
    private Integer outlineId; //课程ID

    @Column(name="doctor_id")
    private Integer doctorId; //医生id

    @Column(name="attach_id")
    private String attachId; //附件表id

    @Column(name="content")
    private String content; //反馈内容

    @Column(name="reply")
    private String reply; //回复内容

    @Column(name="reply_user_id")
    private Integer replyUserId; //

    @Column(name="create_time",updatable=false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name="update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Transient
    private String name;

    @Transient
    private String photo;
    @Transient
    private String path;

    @Transient
    private String datestr;

    @Transient
    private String  courseName;
    @Transient
    private String  doctorName;
    @Transient
    private String  title;
}
