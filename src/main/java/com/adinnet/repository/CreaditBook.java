package com.adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author wangren
 * @Description: 学分模板
 * @create 2018-10-09 15:12
 **/
@Data
@Entity
@Table(name ="tb_cradit_model")
public class CreaditBook extends BaseEntity implements Serializable {

    /**主键*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

   // @Column(name="semester_id")
    @Column(name="semester_id",updatable=false)
    private Integer semesterId;
//    @OneToOne
//    @JoinColumn(name = "semester_id")
//    private Semester semester;

    @Column(name="prove_con")
    private String proveCon;

    @Column(name="headlines")
    private String headlines;

    @Column(name="semester_title")
    private String semesterTitle;

    @Column(name="creadit_name")
    private String creaditName;

    @Column(name="hospital1")
    private String hospital1;

    @Column(name="hospital2")
    private String hospital2;

    @Column(name="chapter1")
    private String chapter1;

    @Column(name="chapter2")
    private String chapter2;

    @Column(name="issue_date")
    private String issueDate;

    @Column(name="is_delete")
    private Integer isDelete;

    /**创建时间*/
    @Column(name="create_time",updatable=false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**修改时间*/
    @Column(name="update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
