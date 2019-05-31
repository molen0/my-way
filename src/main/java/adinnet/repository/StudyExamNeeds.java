package adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by wangx on 2018/9/18.
 */
@Data
@Entity
@Table(name ="tb_study_exam_needs")
public class StudyExamNeeds extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "title")   //标题名
    private String title;
    @Column(name = "photo_url") // 图片地址
    private String photoUrl;
    @Column(name = "text_Area")  // 文本域
    private String textArea;
    @Column(name = "status")  // 0 知识学习 ： 1 必须课程
    private int status;
    @Column(name = "type")  // 类型 0 学习 ： 1 测试
    private int type;
    @Column(name = "is_deleted") // 是否删除 0 删除 ： 1 正常
    private int isDeleted;

    @Column(name = "create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name = "update_time") // 修改时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;




}
