package com.adinnet.controller.m;

import com.adinnet.repository.Doctor;
import com.adinnet.repository.Semester;
import com.adinnet.repository.SemesterDoctor;
import com.adinnet.response.code.CCode;
import com.adinnet.response.json.JsonResult;
import com.adinnet.service.*;
import com.adinnet.utils.ServletUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 在线报名
 * Created by RuanXiang on 2018/9/20.
 */

@RestController
@RequestMapping("m/onlinSignUp")
public class MOnlineSignUpController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private SemesterDoctorService semesterDoctorService;

    @Autowired
    private OnlineSignUpService onlineSignUpService;

    /**
     * 查询用户本学期是否已报过名
     * @return
     */
    @RequestMapping(value = "/isSignUp", method = {RequestMethod.POST})
    public JsonResult isSignUp(HttpServletRequest request){
        try {
            String body = ServletUtils.getContent(request);
            JSONObject jsonObject = JSON.parseObject(body);
            //万达信息系统医生uid
            String uid = jsonObject.getString("uid");
            if(!"".equals(uid)){
                //查询是否已报过名
                Doctor doctor = doctorService.queryByUid(uid);
                JSONObject jb = new JSONObject();
                jb.put("uid",uid);
                if(null != doctor){
                    //查询本学期
                    Semester semester = semesterService.queryThisSemester();
                    if(null != semester){
                        //查看本学期是否已报名
                        SemesterDoctor semesterDoctor = semesterDoctorService.queryIsSignUp(semester.getId(),doctor.getId());
                        if(null != semesterDoctor){
                            Integer doctorId = doctor.getId();
                            jb.put("doctorId",doctorId);
                            jb.put("status",1);
                            jb.put("courseAttr",doctor.getProperty());
                            //万达信息系统token
                            String token = jsonObject.getString("token");
                            //报过名，更新医生信息
                            doctorService.getDoctorInfo(uid,token,doctorId);
                            return JsonResult.OK(jb);
                        }else{
                            jb.put("status",0);
                            return JsonResult.OK(jb);
                        }
                    }else{
                        //本学期暂未开课
                        return JsonResult.build(CCode.C_Semester_NULL);
                    }
                }
                jb.put("status",0);
                return JsonResult.OK(jb);
            }
            return JsonResult.build(CCode.C_PARAM_IS_NULL);
        } catch (IOException e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }

    /**
     * 查询医生信息
     * @return
     */
    @RequestMapping(value = "/getDoctorInfo", method = {RequestMethod.POST})
    public JsonResult getDoctorInfo(HttpServletRequest request){
        try {
            String body = ServletUtils.getContent(request);
            JSONObject jsonObject = JSON.parseObject(body);
            //万达信息系统医生uid
            String uid = jsonObject.getString("uid");
            //万达信息系统token
            String token = jsonObject.getString("token");
            Doctor doctor = doctorService.getDoctorInfo(uid,token,null);
            if(null != doctor){
                return JsonResult.OK(doctor);
            }
            return JsonResult.build(CCode.C_Doctor_NULL);
        } catch (IOException e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }

    /**
     * 保存报名医生信息
     * @return
     */
    @RequestMapping(value = "/saveDoctor", method = {RequestMethod.POST})
    public JsonResult saveDoctor(HttpServletRequest request){
        try {
            String body = ServletUtils.getContent(request);
            JSONObject jsonObject = JSON.parseObject(body);
            //万达信息系统医生uid
            String uid = jsonObject.getString("uid");
            //职称
            String title = jsonObject.getString("title");
            //邮箱
            String email = jsonObject.getString("email");
            //姓名
            String name = jsonObject.getString("name");
            //手机号
            String phone = jsonObject.getString("phone");
            //医生属性
            Integer property = jsonObject.getInteger("property");
            //单位
            String company = jsonObject.getString("company");
            //头像
            String photo = jsonObject.getString("photo");
            //性别
            String gender = jsonObject.getString("gender");
            //区域
            String area = jsonObject.getString("area");
            if("".equals(uid)||"".equals(name)){
                    return JsonResult.build(CCode.C_DOCTOR_IS_NULL);
            }
//            //万达信息系统token
//            String token = jsonObject.getString("token");
//            //万达信息系统token
//            Integer auditingStatus = jsonObject.getInteger("auditingStatus");
//            //查询是否实名
//            if(auditingStatus == 0){
//                Doctor doctor = doctorService.getDoctorInfo(uid,token);
//                if(doctor.getAuditingStatus() == 0){
//                    return JsonResult.build(CCode.C_DOCTOR_Auditing);
//                }
//            }
            Doctor doctor = new Doctor();
            doctor.setTitle(title);
            doctor.setUid(uid);
            doctor.setEmail(email);
            doctor.setName(name);
            doctor.setPhone(phone);
            doctor.setProperty(property);
            doctor.setCompany(company);
            doctor.setPhoto(photo);
            doctor.setGender(gender);
            doctor.setArea(area);
            JSONObject jb = onlineSignUpService.saveSemesterAndDoctor(doctor);
            return JsonResult.OK(jb);
        } catch (IOException e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }
}
