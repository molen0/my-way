package com.adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author wangren
 * @Description: 用户考试记录
 * @create 2018-09-29 16:07
 **/
@Data
@Entity
@Table(name ="tb_examin_user")
public class ExaminUser extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;//主键.

    //@Column(name="user_id")
  //  private String userId;//用户id

    @OneToOne
    @JoinColumn(name = "user_id")
    private Doctor doctor;

    //@Column(name="examin_id")
    //private Integer examinId;//套试题id.

    @OneToOne
    @JoinColumn(name = "examin_id")
    private Examin examin;
    @OneToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;
    @Column(name="credit")
    private String credit;//学分.

    @Column(name="rights")
    private Integer right;//正确题数

    @Column(name="error")
    private Integer error;//错误题数

    @Column(name="is_delete")
    private Integer isDelete;

    @Column(name="create_time",updatable=false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name="update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Transient
    private List<ExaminUserRecord> examinList;

    @Transient
    private String doctorName;

    @Transient
    private String doctorCompany;

    @Transient
    private String semesterName;

    @Transient
    private String semesterCredit;
}
