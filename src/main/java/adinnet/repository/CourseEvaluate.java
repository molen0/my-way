package adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by hasee on 2018/9/18.
 */
@Data
@Entity
@Table(name ="tb_course_evaluate")
public class CourseEvaluate extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id; // 编号


    @Column(name="course_type")
    private Integer courseType; //课程类型 ：1，必学课程；2，知识学习；3，经验分享


    @Column(name="course_id")
    private Integer courseId; //课程ID

    @Column(name="doctor_id")
    private Integer doctorId; //医生id

    @Column(name="impression")
    private String impression; //对视频的印象

    @Column(name="impression_id")
    private String impressionId; //对视频的印象

    @Column(name="expect_course_id")
    private String expectCourseId; //期待上的课程，关联evaluate_tag,多个“，”分割',

    @Column(name="expect_course")
    private String expectCourse;

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
    private String datestr;
    @Transient
    private String  courseName;
    @Transient
    private String  doctorName;


}
