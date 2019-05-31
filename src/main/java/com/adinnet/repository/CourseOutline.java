package com.adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author W.
 * @Description: 课程大纲
 * @create 2018-09-18 10:58
 **/
@Data
@Entity
@Table(name ="tb_course_outline")
public class CourseOutline extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id; //主键id

    @Column(name="course_id")
    private Integer courseId; //课程id

    @Column(name = "name")
    private String name; //章节名称

    @Column(name = "video_time")
    private String videoTime; //视频时长

    @Column(name = "video_path")
    private String videoPath; //视频存放路径

    @Column(name = "outline_introduce")
    private String outlineIntroduce; //大纲介绍

    @Column(name = "sort")
    private Integer sort; //排序

    @Column(name = "user_id")
    private Integer userId; //用户id

    @Column(name = "create_time",updatable=false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime; //创建时间

    @Column(name = "update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime; //更新时间

    @Column(name = "status",updatable=false)
    private Integer status;//状态 1，正常 2，删除

    @Column(name = "m_video_path")
    private Integer mVideoPath;//状态 1，正常 2，删除

    @Transient
    private String rate;//观看视频进度

    @Transient
    private String time;//观看视频时长

    @Transient
    private Integer nums;//观看视频次数

    @Transient
    private String title;

    @Transient
    private Integer courseAttr;

    @Transient
    private String specialistName;

    @OneToOne
    @JoinColumn(name = "course_id_temp")
    private Course course;

}
