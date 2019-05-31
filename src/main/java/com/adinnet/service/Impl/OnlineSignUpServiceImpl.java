package com.adinnet.service.Impl;

import com.adinnet.dao.DoctorMapper;
import com.adinnet.dao.SemesterDoctorMapper;
import com.adinnet.dao.SemesterMapper;
import com.adinnet.ex.BuzEx;
import com.adinnet.repository.Doctor;
import com.adinnet.repository.Semester;
import com.adinnet.repository.SemesterDoctor;
import com.adinnet.response.code.CCode;
import com.adinnet.response.json.JsonResult;
import com.adinnet.service.OnlineSignUpService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by adinnet on 2018/9/26.
 */
@Service
public class OnlineSignUpServiceImpl implements OnlineSignUpService {

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private SemesterDoctorMapper semesterDoctorMapper;

    @Autowired
    private SemesterMapper semesterMapper;

    @Override
    public JSONObject saveSemesterAndDoctor(Doctor doctor) {
        JSONObject jb = new JSONObject();
        Date now = new Date();
        //查看医生信息
        Doctor doctorHas = doctorMapper.queryByUid(doctor.getUid());
        Integer id = null;
        //医生类型
        Integer property = null;
        //查询本学期
        Semester semester = semesterMapper.queryThisSemester();
        Integer semesterId = semester.getId();
        if(null != doctorHas){
            id = doctorHas.getId();
            property = doctorHas.getProperty();
            //查看本学期是否已报过名
            SemesterDoctor semesterDoctorThis = semesterDoctorMapper.queryIsSignUp(semesterId,id);
            if(null != semesterDoctorThis){
                jb.put("doctorId",id);
                jb.put("courseAttr",property);
                throw new BuzEx(CCode.C_Semester_SignUp);
            }
        }else {
            //保存医生
            doctor.setCreateTime(now);
            doctor.setUpdateTime(now);
            doctorMapper.save(doctor);
            id = doctor.getId();
            property = doctor.getProperty();
        }
        if(null != id){
            //保存学期，医生
            SemesterDoctor semesterDoctor = new SemesterDoctor();
            semesterDoctor.setDoctorId(id);
            semesterDoctor.setSemesterId(semesterId);
            semesterDoctor.setCreateTime(now);
            semesterDoctor.setUpdateTime(now);
            semesterDoctorMapper.save(semesterDoctor);
            jb.put("doctorId",id);
            jb.put("courseAttr",property);
        }
        return jb;
    }
}
