package adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author wangren
 * @Description: 课程题目主类
 * @create 2018-09-26 10:55
 **/
@Data
@Entity
@Table(name ="tb_examin")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class Examin extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;//主键.
    @Column(name="type")
    private Integer type;//类型  0：必学课程  1：知识学习

    @Column(name="is_delete")
    private Integer isDelete;
    @Column(name="create_time",updatable=false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name="update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @OneToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Transient
    private String ctitle;
    @Transient
    private Integer ccourseAttr;
    @Transient
    private Integer ccourseType;
    @Transient
    private String SpecialListName;
}
