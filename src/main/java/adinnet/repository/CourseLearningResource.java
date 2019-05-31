package adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by wangx on 2018/9/20.
 */
@Data
@Entity
@Table(name ="tb_course_learning_resource")
public class CourseLearningResource extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "course_id")
    private Integer courseId;   //课程ID

    @Column(name = "name")
    private String name; // 资料名称

    @Column(name = "attach_id")
    private String attachId;      // 关联附件表

    @Column(name = "pdf_path")
    private String pdfPath;    // pdf存放路径

    @Column(name = "user_id")
    private int userId;          // 用户id

    @Column(name = "is_deleted")
    private int isDeleted;      // 是否删除 0 删除 ： 1 正常

    @Column(name = "create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @Column(name = "update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;   // 修改时间

    @Transient
    private String courseName;
    /**课程属性*/
    @Transient
    private Integer courseAttr;
    /**专家名*/
    @Transient
    private String specialistNames;
//
//    @OneToOne
//    @JoinColumn(name = "course_id_temp")
//    private Course course;


}
