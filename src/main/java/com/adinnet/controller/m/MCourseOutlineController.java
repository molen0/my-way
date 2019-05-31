package com.adinnet.controller.m;

import com.adinnet.repository.Course;
import com.adinnet.repository.CourseOutline;
import com.adinnet.response.code.CCode;
import com.adinnet.response.json.JsonResult;
import com.adinnet.service.CourseOutlineService;
import com.adinnet.service.CourseService;
import com.adinnet.service.CourseStudyRateService;
import com.adinnet.support.annotation.MRequest;
import com.adinnet.utils.ServletUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 课程大纲相关接口
 * author:郭光林
 * date:2018-09-18
 */
@RestController
@RequestMapping("m/courseOutline")
public class MCourseOutlineController {

    @Autowired
    private CourseOutlineService courseOutlineService;

    @Autowired
    private CourseStudyRateService courseStudyRateService;

    @Autowired
    private CourseService courseService;


    /**
     * 获取课程大纲
     * @return
     */
    @RequestMapping(value = "/outlineList", method = {RequestMethod.POST})
    public JsonResult outlineList(HttpServletRequest request){
        try {
            String body = ServletUtils.getContent(request);
            JSONObject jsonObject = JSON.parseObject(body);
            Integer courseId = jsonObject.getInteger("courseId");
            if(courseId != null){
                Map map = courseOutlineService.queryByCourseId(courseId,jsonObject.getInteger("doctorId"));
                if(null != map.get("code")&&"4055".equals(map.get("code"))){
                    return JsonResult.build(CCode.C_DOCTOR_IS_NULL);
                }
                return JsonResult.OK(map);
            }
            return JsonResult.build(CCode.C_PARAM_IS_NULL);
        } catch (IOException e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }

    /**
     * 修改大纲学习进度
     * @param request
     * @return
     */
    @RequestMapping(value="updateOutlineRate",method = {RequestMethod.POST})
    public JsonResult updateOutlineRate(HttpServletRequest request){
        try {
            String body = ServletUtils.getContent(request);
            JSONObject jsonObject = JSON.parseObject(body);
            Integer courseId = jsonObject.getInteger("courseId");
            Integer doctorId = jsonObject.getInteger("doctorId");
            Integer outlineId = jsonObject.getInteger("outlineId");
            String time  = jsonObject.getString("time");
            if(courseId !=null && doctorId !=null && outlineId !=null){
                try{
                    String code = courseStudyRateService.updateStudyRate(courseId,doctorId,outlineId,time);
                    if(!"200".equals(code)){
                        return JsonResult.build(CCode.C_DOCTOR_IS_NULL);
                    }
                }catch(Exception e){
                    return JsonResult.OK();
                }
                return JsonResult.OK();
            }
            return JsonResult.build(CCode.C_PARAM_IS_NULL);
        } catch (IOException e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }

    /**
     * 修改课程学习人数
     * @param request
     * @return
     */
    @RequestMapping(value = "updateCourseNum",method = {RequestMethod.POST})
    public JsonResult updateCourseNum(HttpServletRequest request){
        try{
            String body = ServletUtils.getContent(request);
            JSONObject jsonObject = JSON.parseObject(body);
            Integer courseId = jsonObject.getInteger("courseId");
            Integer num = courseService.updateCourseNum(courseId);
            return JsonResult.OK(num);
        }catch (IOException e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }

    /**
     * 判断是否可以开始考试
     * @param request
     * @return
     */
    @RequestMapping(value="startExam",method = {RequestMethod.POST})
    public JsonResult startExam(HttpServletRequest request){
        try{
            String body = ServletUtils.getContent(request);
            JSONObject jsonObject = JSON.parseObject(body);
            Integer courseId = jsonObject.getInteger("courseId");
            Integer doctorId = jsonObject.getInteger("doctorId");
            if(courseId !=null && doctorId != null){
                boolean result = courseOutlineService.startExam(courseId,doctorId);
                return JsonResult.OK(result);
            }
            return JsonResult.build(CCode.C_PARAM_IS_NULL);
        }catch (IOException e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }
}
