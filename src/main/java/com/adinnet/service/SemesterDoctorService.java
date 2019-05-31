package com.adinnet.service;

import com.adinnet.repository.SemesterDoctor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by adinnet on 2018/9/20.
 */
public interface SemesterDoctorService {
    public void save(SemesterDoctor semesterDoctor);

    public void update(SemesterDoctor semesterDoctor);

    public void delete(SemesterDoctor semesterDoctor);

    /**
     * 查询本学期是否已报名
     * @return
     */
    public SemesterDoctor queryIsSignUp(Integer semesterId, Integer doctorId);

    public List<SemesterDoctor> querysemesterId(Integer semesterId);
}
