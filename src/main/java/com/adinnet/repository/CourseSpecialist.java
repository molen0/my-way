package com.adinnet.repository;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by adinnet on 2018/9/21.
 */
@Data
@Entity
@Table(name ="tb_course_specialist")
public class CourseSpecialist {
    /**主键*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="course_id")
    private Integer courseId;

    @Column(name="specialist_id")
    private Integer specialistId;
}
