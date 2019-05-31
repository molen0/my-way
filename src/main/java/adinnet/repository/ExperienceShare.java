package adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@Table(name ="tb_experience_share")
public class ExperienceShare extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;//主键id

    @Column(name="type")
    private Integer type; //1，政府部门；2，医疗机构；3，居民经验

    @Column(name = "photo")
    private String photo;//经验分享图片

    @Column(name="title")
    private String title; //标题

    @Column(name="hospital")
    private String hospital; //医院

    @Column(name="introduce")
    private String introduce;//介绍

    @Column(name="file_path")
    private String filePath;//文件路径

    @Column(name="file_type")
    private Integer fileType;//文件类型：1,视频；2,文档

    @Column(name="user_id")
    private Integer userId;//添加人

    @Column(name = "attach_id")
    private String attachId;//关联附件attach表

    @Column(name = "fileName")
    private String fileName; //文件名称

    @Column(name="create_time",updatable=false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间

    @Column(name="update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;//更新时间
}
