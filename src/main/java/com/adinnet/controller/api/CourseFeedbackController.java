package com.adinnet.controller.api;

import com.adinnet.common.status.CourseType;
import com.adinnet.repository.Course;
import com.adinnet.repository.CourseFeedback;
import com.adinnet.repository.Doctor;
import com.adinnet.repository.Page1;
import com.adinnet.repository.dto.CourseEvaluateDto;
import com.adinnet.service.CourseFeedbackService;
import com.adinnet.service.CourseService;
import com.adinnet.service.DoctorService;
import com.adinnet.utils.ReturnUtil;
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
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by hasee on 2018/9/18.
 */
@Controller
@RequestMapping("/api/courseFeedback")
public class CourseFeedbackController {

    @Autowired
    private CourseFeedbackService courseFeedbackService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private DoctorService doctorService;

    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "courseFeedback/index";
    }

/*    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(CourseFeedback courseFeedback) {
        ModelMap map = new ModelMap();
        Page<CourseFeedback> page = courseFeedbackService.pageList(courseFeedback);
        map.put("pageInfo", page);
        map.put("queryParam", courseFeedback);
        return ReturnUtil.Success("加载成功", map, null);
    }*/


    @RequestMapping(value = "/list2", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(CourseFeedback courseFeedback) {
        ModelMap map = new ModelMap();
        Page<CourseFeedback> page = courseFeedbackService.pageList(courseFeedback);
        map.put("pageInfo", page);
        map.put("queryParam", courseFeedback);
        return ReturnUtil.Success("加载成功", map, null);
    }

    @RequestMapping(value = "/list1", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list1(CourseFeedback courseFeedback) {
        ModelMap map = new ModelMap();
        Page<List<Map<String, Object>>> page = courseFeedbackService.pageList2(courseFeedback);
        map.put("pageInfo", page);
        map.put("queryParam", courseFeedback);
        return ReturnUtil.Success("加载成功", map, null);
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(CourseEvaluateDto courseEvaluateDto) {
        try{
            ModelMap map = new ModelMap();
            map.put("queryParam",courseEvaluateDto);
            Page1<List<Map<String, Object>>> page = courseFeedbackService.pageList1(courseEvaluateDto);
            map.put("pageInfo",page);
            return ReturnUtil.Success("加载成功", map, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("加载失败", null, null);
        }
    }


/*    @RequestMapping(value = "/from", method = {RequestMethod.GET})
    public String from(CourseFeedback courseFeedback, Model model) {
        if (null != courseFeedback.getId()) {
            courseFeedback = courseFeedbackService.getOne(courseFeedback.getId());
        }

        Integer courseId=courseFeedback.getCourseId();
        if(courseId!=null){
        Course course=courseService.getOne(courseId);
            if(course!=null){
        Integer courseType=course.getCourseType();
        String  courseTypeName="";
        if(CourseType.MUST.getCode()==courseType){
            courseTypeName=CourseType.MUST.getName();
        }else if(CourseType.LEARNING.getCode()==courseType){
            courseTypeName=CourseType.LEARNING.getName();
        }else{
            courseTypeName=CourseType.EXPERIENCE.getName();
        }

            model.addAttribute("courseType", courseTypeName);
            }else{
                model.addAttribute("courseType", null);
            }

        }

        model.addAttribute("id", courseFeedback.getId());
        model.addAttribute("courseId", courseFeedback.getCourseId());
        model.addAttribute("courseFeedback", courseFeedback);

        return "courseFeedback/from";
    }*/


    @RequestMapping(value = "/from", method = {RequestMethod.GET})
    public String from(CourseFeedback courseFeedback, Model model) {
        if (null != courseFeedback.getId()) {
            courseFeedback = courseFeedbackService.getOne(courseFeedback.getId());
        }
        Integer courseId=courseFeedback.getCourseId();
        if(courseId!=null){
            Course course=courseService.getOne(courseId);
            Integer courseType=course.getCourseType();
            String  courseTypeName="";
            if(CourseType.MUST.getCode()==courseType){
                courseTypeName=CourseType.MUST.getName();
            }else if(CourseType.LEARNING.getCode()==courseType){
                courseTypeName=CourseType.LEARNING.getName();
            }else{
                courseTypeName=CourseType.EXPERIENCE.getName();
            }

            if(course!=null){
                model.addAttribute("courseName", course.getTitle());
                model.addAttribute("courseId", courseId);
                model.addAttribute("courseTypeName", courseTypeName);

            }else {
                model.addAttribute("courseName", null);
                model.addAttribute("courseId", null);
                model.addAttribute("courseTypeName", null);

            }
        }

        Integer doctorId=courseFeedback.getDoctorId();
        if(doctorId!=null){
            Doctor doctor=doctorService.getOne(doctorId);
            if(doctor!=null){
                model.addAttribute("doctorName", doctor.getName());
                model.addAttribute("doctorId", doctor.getId());
                model.addAttribute("phone", doctor.getPhone());
            }else{
                model.addAttribute("doctorName", null);
                model.addAttribute("doctorId", null);
                model.addAttribute("phone", null);
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        model.addAttribute("createTime", sdf.format(courseFeedback.getCreateTime()));
        model.addAttribute("id", courseFeedback.getId());
        model.addAttribute("courseFeedback", courseFeedback);
        return "courseFeedback/from";
    }

    @RequestMapping(value = "/update", method = {RequestMethod.GET})
    public String update(CourseFeedback courseFeedback, Model model) {
        if (null != courseFeedback.getId()) {
            courseFeedback = courseFeedbackService.getOne(courseFeedback.getId());
        }

        Integer courseId=courseFeedback.getCourseId();
        if(courseId!=null){
            Course course=courseService.getOne(courseId);
            if(course!=null){
                Integer courseType=course.getCourseType();
                String  courseTypeName="";
                if(CourseType.MUST.getCode()==courseType){
                    courseTypeName=CourseType.MUST.getName();
                }else if(CourseType.LEARNING.getCode()==courseType){
                    courseTypeName=CourseType.LEARNING.getName();
                }else{
                    courseTypeName=CourseType.EXPERIENCE.getName();
                }

                model.addAttribute("courseType", courseTypeName);
            }else{
                model.addAttribute("courseType", null);
            }

        }

        model.addAttribute("id", courseFeedback.getId());
        model.addAttribute("courseId", courseFeedback.getCourseId());
        model.addAttribute("courseFeedback", courseFeedback);

        return "courseFeedback/update";
    }


    @RequestMapping(value = "/detail", method = {RequestMethod.GET})
    public String detail(CourseFeedback courseFeedback, Model model) {
        if (null != courseFeedback.getId()) {
            courseFeedback = courseFeedbackService.getOne(courseFeedback.getId());
        }
        Integer courseId=courseFeedback.getCourseId();
        if(courseId!=null){
            Course course=courseService.getOne(courseId);
            Integer courseType=course.getCourseType();
            String  courseTypeName="";
            if(CourseType.MUST.getCode()==courseType){
                courseTypeName=CourseType.MUST.getName();
            }else if(CourseType.LEARNING.getCode()==courseType){
                courseTypeName=CourseType.LEARNING.getName();
            }else{
                courseTypeName=CourseType.EXPERIENCE.getName();
            }

            if(course!=null){
                model.addAttribute("courseName", course.getTitle());
                model.addAttribute("courseId", courseId);
                model.addAttribute("courseType", courseTypeName);

            }else {
                model.addAttribute("courseName", null);
                model.addAttribute("courseId", null);
                model.addAttribute("courseType", null);

            }
        }

        Integer doctorId=courseFeedback.getDoctorId();
        if(doctorId!=null){
          Doctor doctor=doctorService.getOne(doctorId);
          if(doctor!=null){
            model.addAttribute("doctorName", doctor.getName());
              model.addAttribute("phone", doctor.getPhone());
           }else{
              model.addAttribute("doctorName", null);
              model.addAttribute("phone", null);
          }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        model.addAttribute("createTime", sdf.format(courseFeedback.getCreateTime()));
        model.addAttribute("id", courseFeedback.getId());
        model.addAttribute("courseFeedback", courseFeedback);
        return "courseFeedback/detail";
    }


    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap save(CourseFeedback courseFeedback, BindingResult result) {
        if (result.hasErrors()) {
            for (ObjectError er : result.getAllErrors()) return ReturnUtil.Error(er.getDefaultMessage(), null, null);
        }
        try {
            if (null == courseFeedback.getId()) {
              //  courseFeedbackService.saveEvaluateTag(courseFeedback);
                courseFeedbackService.saveFeedback(courseFeedback);
            } else {

                courseFeedbackService.updateCourseFeedback(courseFeedback);
            }
            return ReturnUtil.Success("操作成功", null, "/api/courseFeedback/index");
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }

    @RequestMapping(value = "/edit", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap edit(CourseFeedback courseFeedback, BindingResult result) {
        if (result.hasErrors()) {
            for (ObjectError er : result.getAllErrors()) return ReturnUtil.Error(er.getDefaultMessage(), null, null);
        }
        try {
            if (null == courseFeedback.getId()) {
                //  courseFeedbackService.saveEvaluateTag(courseFeedback);
                courseFeedbackService.saveFeedback(courseFeedback);
            } else {

                courseFeedbackService.updateCourseFeedback(courseFeedback);
            }
            return ReturnUtil.Success("操作成功", null, "/api/courseFeedback/index");
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap delete(Integer[] ids) {
        try {
            if (ids != null) {
                if (StringUtils.isNotBlank(ids.toString())) {
                    for (Integer id : ids) {
                        courseFeedbackService.del(id);
                    }
                }
                //return ReturnUtil.Success("删除成功", null, null);
                return ReturnUtil.Success("删除成功", null, "/api/courseFeedback/index");
            } else {
                return ReturnUtil.Error("删除失败", null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("删除失败", null, null);
        }
    }
}
