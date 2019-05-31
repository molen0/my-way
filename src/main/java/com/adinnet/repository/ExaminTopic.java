package com.adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author wangren
 * @Description: 考试题目
 * @create 2018-09-26 11:11
 **/
@Data
@Entity
@Table(name ="tb_examin_topic")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class ExaminTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;//主键.
    @Column(name="examin_id")
    private Integer examinId;//课程考试主表id.
    @Column(name="topic_name")
    private String topicName;//题目描述

    @Column(name="topic_answer")
    private String topicAnswer;//正确答案
    @Column(name="orders")
    private Integer order;//排序

    @Column(name="type")
    private Integer type;//1单选2多选、

    @Column(name="is_delete")
    private Integer isDelete;

    @Column(name="remark")
    private String remark;

    @Column(name="create_time",updatable=false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name="update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Transient
    private List<ExaminTopicAnswer> answerList;
}
