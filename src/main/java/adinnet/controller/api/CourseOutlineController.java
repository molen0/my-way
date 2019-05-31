package adinnet.controller.api;

import com.adinnet.common.QiniuConfig;
import com.adinnet.dao.CourseMapper;
import com.adinnet.http.HttpUtils;
import com.adinnet.repository.*;
import com.adinnet.service.CourseOutlineService;
import com.adinnet.service.CourseService;
import com.adinnet.service.EvaluateTagService;
import com.adinnet.utils.ReturnUtil;
import com.adinnet.utils.qiniu.QiniuUtils;
import com.qiniu.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/19.
 */

@Controller
@RequestMapping("/api/courseOutline")
@Slf4j
public class CourseOutlineController {

    @Autowired
    private CourseOutlineService courseOutlineService;

    @Autowired
    private CourseService courseService;

    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "courseOutline/index";
    }


    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(CourseOutline courseOutline) {
        ModelMap map = new ModelMap();
        //Page<CourseOutline> page = courseOutlineService.pageList(courseOutline);
        //Page<List<Map<String,CourseOutline>>> page = courseOutlineService.pageList1(courseOutline);
        map.put("queryParam",courseOutline);
        PageBean<CourseOutline> pageBean = courseOutlineService.pageList3(courseOutline);
        map.put("pageInfo",pageBean);
        return ReturnUtil.Success("加载成功", map, null);
    }

    @RequestMapping(value = "/from", method = {RequestMethod.GET})
    public String from(CourseOutline courseOutline, Model model) {
        if (null != courseOutline.getId()) {
            courseOutline = courseOutlineService.getOne(courseOutline.getId());
        }
        model.addAttribute("id", courseOutline.getId());
        model.addAttribute("courseOutline", courseOutline);
        return "courseOutline/from";
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap save(@RequestParam(value="video",required = false) MultipartFile file,CourseOutline courseOutline){
        try{
            if(null == courseOutline.getId()){//保存
                return courseOutlineService.saveCourseOutline(courseOutline,file);
            }else{//更新
                return courseOutlineService.updateCourseOutline(courseOutline, file);
            }
        }catch (Exception e) {
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
                        courseOutlineService.del(id);
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

    @RequestMapping(value = "getCourse")
    @ResponseBody
    public ModelMap getCourse(Course course) {
        Page<Course> list = courseService.pageList(course);
        return ReturnUtil.Success("成功",list);
    }
}
