package adinnet.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 课程
 * Created by RuanXiang on 2018/9/18.
 */
@Data
@Entity
@Table(name ="tb_course")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class Course extends BaseEntity implements Serializable {

    /**主键*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    /**课程属性：0,乡村医生；1，全科医生*/
    @Column(name="course_attr")
    private Integer courseAttr;

    /**课程类型：1，必学课程；2，知识学习；3，经验分享*/
    @Column(name="course_type")
    private Integer courseType;

    /**课程名称*/
    @Column(name="title")
    private String title;

    /**课程图片*/
    @Column(name="photo")
    private String photo;

    /**视频展示图*/
    @Column(name="image")
    private String image;

    /**课程介绍*/
    @Column(name="introduce")
    private String introduce;

    /**学习人数*/
    @Column(name="study_num")
    private Integer studyNum;

    /**创建人id*/
    @Column(name="user_id")
    private Integer userId;

    /**学期id*/
    @Column(name="semester_id")
    private Integer semesterId;

    /**状态 0：停用  1：正常*/
    @Column(name="status")
    private Integer status;

    /**排序 越小越靠前*/
    @Column(name="indexs")
    private Integer indexs;

    /**是否考试 0：否  1：是*/
    @Column(name="is_open")
    private Integer isOpen;

    /**章数*/
    @Transient
    private Integer chapterCount;

    /**章数*/
    @Transient
    private String semesterName;

    /**专家*/
    @Transient
    private String specialist;

    /**专家id*/
    @Transient
    private String specialistCodes;

    /**专家名称*/
    @Column(name="specialist_names")
    private String specialistNames;

    /**创建时间*/
    @Column(name="create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**更新时间*/
    @Column(name="update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
