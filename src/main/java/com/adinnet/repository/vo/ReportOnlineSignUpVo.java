package com.adinnet.repository.vo;

import lombok.Data;

/**
 * 在线报名数据统计
 * Created by RuanXiang on 2018/11/8.
 */
@Data
public class ReportOnlineSignUpVo {

    private Integer id;

    //区域名称
    private String areaName;

    //在编总人数
    private String inCount;

    //报名人数
    private String signUpCount;

    //学习人数
    private String learnCount;

    //考试人数
    private String studyCount;

    //培训率
    private String percentLearnCount;

    //考试率
    private String percentStudyCount;

    //完成考试人数
    private String courseCount;

    //完成考试率
    private String percentCourseCount;
}
