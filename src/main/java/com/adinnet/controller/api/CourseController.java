package com.adinnet.controller.api;

import com.adinnet.repository.Course;
import com.adinnet.repository.Semester;
import com.adinnet.repository.Specialist;
import com.adinnet.service.CourseService;
import com.adinnet.service.SemesterService;
import com.adinnet.service.SpecialistService;
import com.adinnet.utils.ReturnUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.util.List;

/**
 * 课程
 * Created by RuanXiang on 2018/9/19.
 */
@Controller
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private SpecialistService specialistService;

    @RequestMapping(value = "/index", method = {RequestMethod.GET},produces="text/html; charset=UTF-8")
    public String index(String ctitle,String cspecialist,String ccourseAttr,String ccourseType,String csemesterId,Model model) {
        try{
            ctitle = java.net.URLDecoder.decode(ctitle,"UTF-8");
            cspecialist = java.net.URLDecoder.decode(cspecialist,"UTF-8");
        }catch(Exception ex){
            ex.printStackTrace();
        }
        model.addAttribute("ctitle", ctitle);
        model.addAttribute("cspecialist", cspecialist);
        model.addAttribute("ccourseAttr", ccourseAttr);
        model.addAttribute("ccourseType", ccourseType);
        model.addAttribute("csemesterId", csemesterId);
        return "course/index";
    }

    //列表
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(Course course) {
        ModelMap map = new ModelMap();
        Page<Course> page = courseService.pageList(course);
        map.put("pageInfo", page);
        map.put("queryParam", course);
        return ReturnUtil.Success("加载成功", map, null);
    }

    //编辑
    @RequestMapping(value = "/from", method = {RequestMethod.GET})
    public String from(Course course, Model model,String ctitle,String cspecialist,String ccourseAttr,String ccourseType,String csemesterId) {
        if (null != course.getId()) {
            Integer courseId = course.getId();
            course = courseService.queryById(courseId);
            //主讲专家
            List<Specialist> list = specialistService.querySpecialistByCourse(courseId);
            String specialists = "";
            String specialistCodes = "";
            for(Specialist specialist:list){
                String name = specialist.getName();
                Integer code = specialist.getId();
                specialists = name +";"+specialists;
                specialistCodes = code.toString()+";"+specialistCodes;
            }
            course.setSpecialist(specialists);
            course.setSpecialistCodes(specialistCodes);
        }
        model.addAttribute("id", course.getId());
        model.addAttribute("course", course);
        model.addAttribute("ctitle", ctitle);
        model.addAttribute("cspecialist", cspecialist);
        model.addAttribute("ccourseAttr", ccourseAttr);
        model.addAttribute("ccourseType", ccourseType);
        model.addAttribute("csemesterId", csemesterId);
        return "course/from";
    }

    //获取学期列表
    @RequestMapping(value = "getSemester")
    @ResponseBody
    public ModelMap getSemester(Semester semester) {
        Page<Semester> list = semesterService.pageList(semester);
        return ReturnUtil.Success("成功",list);
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public String save(@RequestParam("photoU") MultipartFile file,@RequestParam("imageU") MultipartFile imageFile, Course course,@RequestParam("ctitle") String ctitle,@RequestParam("cspecialist") String cspecialist,@RequestParam("ccourseAttr") String ccourseAttr,@RequestParam("ccourseType") String ccourseType,@RequestParam("csemesterId") String csemesterId){
        try{
            if(null == course.getId()){//保存
                courseService.saveCourse(course,file,imageFile);
            }else{//更新
                courseService.updateCourse(course, file,imageFile);
            }
            //处理中文
            ctitle = URLEncoder.encode(ctitle,"UTF-8");
            cspecialist = URLEncoder.encode(cspecialist,"UTF-8");
            return "redirect:/api/course/index?ctitle="+ctitle+"&cspecialist="+cspecialist+"&ccourseAttr="+ccourseAttr+"&ccourseType="+ccourseType+"&csemesterId="+csemesterId;
        }catch (Exception e) {
            e.printStackTrace();
            return "";
            //return ReturnUtil.Error("操作失败", null, null);
        }
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap delete(Integer[] ids) {
        try {
            if (ids != null) {
                if (StringUtils.isNotBlank(ids.toString())) {
                    for (Integer id : ids) {
                        courseService.del(id);
                    }
                }
                return ReturnUtil.Success("删除成功", null, null);
            } else {
                return ReturnUtil.Error("删除失败", null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("删除失败", null, null);
        }
    }

    //获取专家
    @RequestMapping(value = "getSpecialist")
    @ResponseBody
    public ModelMap getSpecialist(Integer id) {
        List<Specialist> list = specialistService.getList(id);
        return ReturnUtil.Success("成功",list);
    }

    //新增专家名处理
    @RequestMapping(value = "/doSpecialist")
    @ResponseBody
    public ModelMap doSpecialist() {
        courseService.doSpecialist();
        return ReturnUtil.Success("成功",null);
    }

}
