package adinnet.controller.api;

import com.adinnet.common.status.CourseType;
import com.adinnet.repository.Course;
import com.adinnet.repository.CourseEvaluate;
import com.adinnet.repository.Doctor;
import com.adinnet.repository.Page1;
import com.adinnet.repository.dto.CourseEvaluateDto;
import com.adinnet.service.CourseEvaluateService;
import com.adinnet.service.CourseService;
import com.adinnet.service.DoctorService;
import com.adinnet.utils.ReturnUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by hasee on 2018/9/18.
 */
@Controller
@RequestMapping("/api/courseEvaluate")
public class CourseEvaluateController {

    @Autowired
    private CourseEvaluateService courseEvaluateService;

    @Autowired
    private CourseService courseService;


    @Autowired
    private DoctorService doctorService;

    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "courseEvaluate/index";
    }

   @RequestMapping(value = "/list2", method = {RequestMethod.GET})
       @ResponseBody
       public ModelMap list(CourseEvaluate courseEvaluate) {
        ModelMap map = new ModelMap();
        Page<CourseEvaluate> page = courseEvaluateService.pageList(courseEvaluate);
        map.put("pageInfo", page);
        map.put("queryParam", courseEvaluate);
        return ReturnUtil.Success("加载成功", map, null);
    }



/*    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list1(CourseEvaluateDto courseEvaluateDto) {
        ModelMap map = new ModelMap();
        Page<List<Map<String, Object>>> page = courseEvaluateService.pageList1(courseEvaluateDto);
        map.put("pageInfo", page);
        map.put("queryParam", courseEvaluateDto);
        return ReturnUtil.Success("加载成功", map, null);
    }*/

   @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(CourseEvaluateDto courseEvaluateDto) {
        try{
            ModelMap map = new ModelMap();
            map.put("queryParam",courseEvaluateDto);
            Page1<List<Map<String, Object>>> page = courseEvaluateService.pageList1(courseEvaluateDto);
            map.put("pageInfo",page);
            return ReturnUtil.Success("加载成功", map, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("加载失败", null, null);
        }
    }

    @RequestMapping(value = "/detail", method = {RequestMethod.GET})
    public String detail(CourseEvaluate courseEvaluate, Model model) {
        if (null != courseEvaluate.getId()) {
            courseEvaluate = courseEvaluateService.getOne(courseEvaluate.getId());
        }
        Integer courseId=courseEvaluate.getCourseId();
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

        Integer doctorId=courseEvaluate.getDoctorId();
        if(doctorId!=null){
            Doctor doctor=doctorService.getOne(doctorId);
            if(doctor!=null){
                model.addAttribute("doctorName", doctor.getName());
            }else{
                model.addAttribute("doctorName", null);
            }
        }
        model.addAttribute("id", courseEvaluate.getId());
        model.addAttribute("courseEvaluate", courseEvaluate);
        return "courseEvaluate/detail";
    }


    @RequestMapping(value = "/delete", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap delete(Integer[] ids) {
        try {
            if (ids != null) {
                if (StringUtils.isNotBlank(ids.toString())) {
                    for (Integer id : ids) {
                        courseEvaluateService.del(id);
                    }
                }
                return ReturnUtil.Success("删除成功", null, "/api/courseEvaluate/index");
            } else {
                return ReturnUtil.Error("删除失败", null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("删除失败", null, null);
        }
    }

}
