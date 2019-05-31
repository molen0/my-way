package adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by adinnet on 2018/9/20.
 */
@Data
@Entity
@Table(name ="tb_specialist")
public class Specialist extends BaseEntity implements Serializable {
    /**主键*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    /**专家名称*/
    @Column(name="name")
    private String name;

    /**职称*/
    @Column(name="title")
    private String title;

    /**所属单位*/
    @Column(name="hospital")
    private String hospital;

    /**介绍*/
    @Column(name="introduce")
    private String introduce;

    /**头像*/
    @Column(name="photo")
    private String photo;

    /**删除状态  0:删除   1：正常*/
    @Column(name="is_delete")
    private Integer isDelete;

    /**是否被选中 0：否 1：是*/
    @Transient
    private Integer checked;

    /**创建时间*/
    @Column(name="create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**更新时间*/
    @Column(name="update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
