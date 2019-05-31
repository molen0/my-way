package adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2018/10/9.
 */
@Data
@Entity
@Table(name ="tb_exam_answer_resource")
public class ExamAnswerResource extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "course_id")
    private Integer courseId;   //课程ID

    @Column(name = "attach_id")
    private String attachId;      // 关联附件表

    @Column(name = "pdf_path")
    private String pdfPath;    // pdf存放路径
    @Column(name = "pdf_name")
    private String pdfName; // pdf资料名称
    @Column(name = "video_path")
    private String videoPath;    // 视频存放路径

    @Column(name = "video_time")
    private String videoTime;    // pdf存放路径

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
}
