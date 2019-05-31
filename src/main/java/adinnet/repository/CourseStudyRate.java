package adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author W.
 * @Description: 课程章节学习进度
 * @create 2018-09-18 11:20
 **/
@Data
@Entity
@Table(name ="tb_course_study_rate")
public class CourseStudyRate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id; //主键id

    @Column(name="course_id")
    private Integer courseId; //课程id

    @Column(name = "outline_id")
    private Integer outlineId; //大纲id

    @Column(name = "doctor_id")
    private Integer doctorId; //医生id

    @Column(name = "rate")
    private String rate; //进度

    @Column(name = "time")
    private String time; //观看视频时长

    @Column(name = "nums")
    private Integer nums;

    @Column(name = "create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime; //创建时间

    @Column(name = "update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime; //更新时间

    @Column(name="unique_key")
    private String uniqueKey;
}
