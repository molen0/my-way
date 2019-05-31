package com.adinnet.controller.api;


import com.adinnet.ex.BuzEx;
import com.adinnet.repository.*;
import com.adinnet.repository.dto.CourseEvaluateDto;
import com.adinnet.repository.dto.CourseLearningResourceDto;
import com.adinnet.response.code.CCode;
import com.adinnet.response.json.JsonResult;
import com.adinnet.service.CourseEvaluateService;
import com.adinnet.service.CourseLearningResourceService;
import com.adinnet.utils.ReturnUtil;
import com.adinnet.utils.ServletUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by wangx on 2018/9/20.
 */
@Controller
@RequestMapping("/api/courseLearningResource")
public class CourseLearningResourceController {
    @Autowired
    private CourseLearningResourceService courseLearningResourceService;


    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "courseLearningResource/index";
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(CourseLearningResource courseLearningResource) {
        ModelMap map = new ModelMap();
        map.put("queryParam", courseLearningResource);
        PageBean<CourseLearningResource> pageBean = courseLearningResourceService.pageList3(courseLearningResource);
        map.put("pageInfo", pageBean);

        return ReturnUtil.Success("加载成功", map, null);
    }

    @RequestMapping(value = "/from", method = {RequestMethod.GET})
    public String from(CourseLearningResource courseLearningResource, Model model) {
        if (null != courseLearningResource.getId()) {
            courseLearningResource = courseLearningResourceService.getOne(courseLearningResource.getId());
        }
        model.addAttribute("courseLearningResource", courseLearningResource);
        return "courseLearningResource/from";
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap save(CourseLearningResource courseLearningResource, BindingResult result, @RequestParam(value = "pdffile",required = false) MultipartFile file ) {
        if (result.hasErrors()) {
            for (ObjectError er : result.getAllErrors()) return ReturnUtil.Error(er.getDefaultMessage(), null, null);
        }
        try {
            if (null == courseLearningResource.getId()) {
                return courseLearningResourceService.save(courseLearningResource, file);
            } else {
                return courseLearningResourceService.update(courseLearningResource, file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap delete(Integer[] ids) {
        try {
            int id = ids[0];
            courseLearningResourceService.deleteById(id);
            return ReturnUtil.Success("操作成功", null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }

    @RequestMapping(value = "/photoList", method = {RequestMethod.POST})
    public ModelMap outlineList(String attachId) {
        try {
            JSONObject jo = null;
            if (null != attachId) {
                jo = courseLearningResourceService.findPdfByphoto(attachId);
            }
            if (null != jo) {
                return ReturnUtil.Success("操作成功", jo.get("attach"), "/api/courseLearningResource/index");
            }
            return ReturnUtil.Error("操作失败", null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }

    }
    @RequestMapping(value = "getCourse")
    @ResponseBody
    public ModelMap getCourse() {
        List<Course> list = courseLearningResourceService.queryListAllExists();
        return ReturnUtil.Success("成功",list);
    }


}
