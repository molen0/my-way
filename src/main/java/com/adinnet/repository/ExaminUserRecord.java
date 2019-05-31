package com.adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author wangren
 * @Description: 用户考试记录表
 * @create 2018-09-30 9:29
 **/
@Data
@Entity
@Table(name ="tb_examin_user_record")
public class ExaminUserRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;//主键.

    @Column(name="examin_user_id")
    private String examinUserId;//用户id

    @Column(name="record_id")
    private Integer recordId;//用户id
   // @Column(name="topic_id")
   // private Integer topicId;//试题id.

    @Column(name="user_answer")
    private String userAnswer;//用户答案.

    @Column(name="isFlag")
    private Integer isFlag;//1对0错

    @Column(name="examin_id")
    private Integer examinId;//套试题id

    @OneToOne
    @JoinColumn(name = "topic_id")
    private ExaminTopic examinTopic;

    @Column(name="is_delete")
    private Integer isDelete;

    @Column(name="create_time",updatable=false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name="update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
