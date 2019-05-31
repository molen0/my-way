package com.adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author wangren
 * @Description: 群发邮件模板
 * @create 2018-12-13 11:52
 **/
@Data
@Entity
@Table(name ="tb_email_book")
public class EmailBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

   // @Column(name = "semester_id",updatable=false)
    @Transient
    private Integer semesterId;
    @OneToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @Transient
    private String sendDate;

    @Column(name = "tittle")
    private String tittle;

    @Column(name = "content")
    private String content;

    @Transient
    private String handBookUrl;


    @Column(name = "is_delete")
    private int isDelete;      // 是否删除 0 删除 ： 1 正常

    @Column(name = "create_time",updatable=false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name = "update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;   // 修改时间
}
