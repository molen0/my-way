package com.adinnet.controller.m;

import com.adinnet.response.code.CCode;
import com.adinnet.response.json.JsonResult;
import com.adinnet.service.CourseLearningResourceService;
import com.adinnet.service.StudyExamNeedsService;
import com.adinnet.utils.ServletUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by wangx on 2018/9/20.
 * 学习资源相关接口
 */
@RestController
@RequestMapping("m/courseLearningResource")
public class MCourseLearningResourceController {

    @Autowired
    private CourseLearningResourceService courseLearningResourceService;

    /**
     * 查询所有学习资源
     * @return
     */
    @RequestMapping(value = "/resourceList", method = {RequestMethod.POST})
    public JsonResult resourceList(HttpServletRequest request){
        try {
            String body = ServletUtils.getContent(request);
            if (StringUtils.isNotEmpty(body)) {
                    JSONObject jsonObject = JSONObject.parseObject(body);
                Integer courseId = jsonObject.getInteger("courseId");
                JSONObject jo = null;
                if (null !=courseId) {
                  jo = courseLearningResourceService.queryResourceByCourseId(courseId);
                }
                return JsonResult.OK(jo);
            }
            return JsonResult.build(CCode.C_PARAM_IS_NULL);
        } catch (IOException e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }
    @RequestMapping(value = "/photoList", method = {RequestMethod.POST})
    public JsonResult outlineList(HttpServletRequest request){
        try {
            String body = ServletUtils.getContent(request);
            if (StringUtils.isNotEmpty(body)) {
                JSONObject jsonObject = JSON.parseObject(body);
               String  attachId  = jsonObject.getString("attachId");
                JSONObject jo =null;
                if (null != attachId){
                    jo = courseLearningResourceService.findPdfByphoto(attachId);
                }
                return JsonResult.OK(jo);
            }
            return JsonResult.build(CCode.C_PARAM_IS_NULL);
        } catch (IOException e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }

}
