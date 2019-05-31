package com.adinnet.controller.api;

import com.adinnet.repository.Course;
import com.adinnet.repository.CourseLearningResource;
import com.adinnet.repository.ExamAnswerResource;
import com.adinnet.repository.PageBean;
import com.adinnet.repository.dto.CourseLearningResourceDto;
import com.adinnet.repository.dto.ExamAnswerResourceDto;
import com.adinnet.service.CourseService;
import com.adinnet.service.ExamAnswerResourceService;
import com.adinnet.utils.ReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
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

import java.util.List;
import java.util.Map;

/**
 * @author wangren
 * @Description: 答案管理
 * @create 2018-09-29 10:30
 **/
@Controller
@RequestMapping("/api/answer")
public class AnswerController {
    @Autowired
    private ExamAnswerResourceService examAnswerResourceService;
    @Autowired
    private CourseService courseService;

    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "answer/index";
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(ExamAnswerResource examAnswerResource) {
        ModelMap map = new ModelMap();
        map.put("queryParam", examAnswerResource);
        PageBean<ExamAnswerResource> pageBean = examAnswerResourceService.pageList3(examAnswerResource);

        map.put("pageInfo", pageBean);

        return ReturnUtil.Success("加载成功", map, null);
    }

    @RequestMapping(value = "/from", method = {RequestMethod.GET})
    public String from(ExamAnswerResource examAnswerResource, Model model) {
        if (null != examAnswerResource.getId()) {
            examAnswerResource = examAnswerResourceService.getOne(examAnswerResource.getId());
        }
        model.addAttribute("examAnswerResource", examAnswerResource);
        return "answer/from";
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap save(ExamAnswerResource examAnswerResource, BindingResult result, @RequestParam(value = "pdffile" ,required = false ) MultipartFile file,@RequestParam(value = "video" ,required = false) MultipartFile videoFile) {
//        if (result.hasErrors()) {
//            for (ObjectError er : result.getAllErrors()) return ReturnUtil.Error(er.getDefaultMessage(), null, null);
//        }
        try {
            if (null == examAnswerResource.getId()) {
                return examAnswerResourceService.save(examAnswerResource, file,videoFile);
            } else {
                return examAnswerResourceService.update(examAnswerResource, file,videoFile);
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
            examAnswerResourceService.deleteById(id);
            return ReturnUtil.Success("操作成功", null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }

    @RequestMapping(value = "getCourse")
    @ResponseBody
    public ModelMap getCourse(Integer courseId) {
        List<Course> list = courseService.queryListAllExists(courseId);
        return ReturnUtil.Success("成功",list);
    }
}
