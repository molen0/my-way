package com.adinnet.controller.m;

import com.adinnet.response.code.CCode;
import com.adinnet.response.json.JsonResult;
import com.adinnet.service.CourseService;
import com.adinnet.utils.ServletUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 课程
 * Created by RuanXiang on 2018/9/20.
 */
@RestController
@RequestMapping("m/course")
public class MCourseController {

    @Autowired
    private CourseService courseService;

    /**
     * 首页
     * @return
     */
    @RequestMapping(value = "/index", method = {RequestMethod.POST})
    public JsonResult index(HttpServletRequest request){
        try {
            String body = ServletUtils.getContent(request);
            JSONObject jsonObject = JSON.parseObject(body);
            //医生id
            Integer doctorId = jsonObject.getInteger("doctorId");
            return JsonResult.OK(courseService.queryIndexInfo(doctorId));
        } catch (IOException e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }

    /**
     * 查询课程列表信息
     * @return
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public JsonResult list(HttpServletRequest request){
        try {
            String body = ServletUtils.getContent(request);
            JSONObject jsonObject = JSON.parseObject(body);
            //医生id
            Integer doctorId = jsonObject.getInteger("doctorId");
            //课程类型
            Integer courseType = jsonObject.getInteger("courseType");
            return JsonResult.OK(courseService.queryListByParam(doctorId,courseType));
        } catch (IOException e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }

    /**
     * 课程详情-课程介绍
     * @return
     */
    @RequestMapping(value = "/introduce", method = {RequestMethod.POST})
    public JsonResult introduce(HttpServletRequest request){
        try {
            String body = ServletUtils.getContent(request);
            JSONObject jsonObject = JSON.parseObject(body);
            //课程id
            Integer courseId = jsonObject.getInteger("courseId");
            return JsonResult.OK(courseService.queryIntroduceById(courseId));
        } catch (IOException e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }
}
