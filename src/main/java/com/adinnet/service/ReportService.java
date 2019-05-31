package com.adinnet.service;

import com.adinnet.repository.DoctorArea;
import com.adinnet.repository.Semester;
import com.adinnet.repository.vo.ReportOnlineSignUpVo;
import com.adinnet.repository.vo.ReportVo;

import java.util.List;

/**
 * @author wangren
 * @Description: 学分报表
 * @create 2018-10-31 14:59
 **/
public interface ReportService {

    public List<ReportVo> generalList(Semester semester, Integer property);

    public Semester getSemester(Semester semester);

    public List<DoctorArea> areaList(Integer semesterId, Integer property);

    public Integer totleCrateList(String area, Double crateD, Double crateX, Integer semesterId, Integer property);

    public List<ReportOnlineSignUpVo> getSignUpAndCourseList(Integer semesterId, Integer property);
}
