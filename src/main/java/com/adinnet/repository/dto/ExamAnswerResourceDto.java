package com.adinnet.repository.dto;

import com.adinnet.repository.BaseEntity;
import lombok.Data;

/**
 * Created by Administrator on 2018/10/9.
 */
@Data
public class ExamAnswerResourceDto extends BaseEntity {
    /**
     * 课程id
     */
    private int courseId;
    /**
     * 资源id
     */
    private int resourceId;
    /**
     * 课程名
     */
    private String courseName;
    /**
     * 视频路径
     */
    private String videoPath;
    /**
     * 视频时间
     */
    private String videoTime;
    /**
     *pdf 路径
     */
    private String pdfPath;
    /**
     *
     */
    private String attachId;
}
