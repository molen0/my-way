package com.adinnet.repository.vo;

import com.adinnet.repository.ExaminTopic;
import lombok.Data;

import java.util.List;

/**
 * @author wangren
 * @Description: 考试试题
 * @create 2018-09-29 16:34
 **/
@Data
public class ExaminVo {

    private Integer id;//套主键

    private Integer courseId;//课程主键

    private Integer date;//考试时间

    private List<ExaminTopic> examinList;//试题题目
}
