package com.adinnet.repository.vo;

import lombok.Data;

import java.util.List;


/**
 * @author wangren
 * @Description: 用户考试保存
 * @create 2018-09-29 17:40
 **/
@Data
public class ExaminUserVo {

    private Integer id;

    private String userId;

    private List<AnswerVo> answerList;
}
