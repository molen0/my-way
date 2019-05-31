package com.adinnet.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author wangren
 * @Description: 考试题目选项
 * @create 2018-09-26 11:16
 **/
@Data
@Entity
@Table(name ="tb_examin_topic_answer")
@JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
public class ExaminTopicAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;//主键.
    @Column(name="topic_id")
    private Integer topicId;//题目id.
    @Column(name="answer_name")
    private String answerName;//答案描述

    @Column(name="orders")
    private String order;//排序

    @Column(name="is_delete")
    private Integer isDelete;

    @Column(name="create_time",updatable=false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name="update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
