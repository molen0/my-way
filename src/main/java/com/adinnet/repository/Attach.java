package com.adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by hasee on 2018/9/19.
 */
@Data
@Entity
@Table(name ="tb_attach")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class Attach extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id; // 编号
    @Column(name="attach_id")
    private String attachId; //随机生成attach_id

    @Column(name="file_name")
    private String fileName; //文件名称

    @Column(name="type")
    private Integer type; //附件类型：1，课程反馈 2，课程课件 3，课程学习资源'

    @Column(name="path")
    private String path; //附件存放路径

    @Column(name="file_type")
    private Integer fileType; //文件类型（默认为0）：0，图片；1，视频'

    @Column(name="create_time",updatable=false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name="update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
//    @OneToOne
//    @JoinColumn(name = "attach_id")
//    private CourseFeedback courseFeedback;

}
