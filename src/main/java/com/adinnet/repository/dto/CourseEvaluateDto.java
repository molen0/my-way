package com.adinnet.repository.dto;

import com.adinnet.repository.BaseEntity;
import lombok.Data;

/**
 * Created by hasee on 2018/9/26.
 */
@Data
public class CourseEvaluateDto extends BaseEntity {

    private Integer courseId;

    private Integer doctorId;

    private String  courseName;

    private String  doctorName;
    private String impression;

    private String expectCourse;

    private String content;
}
