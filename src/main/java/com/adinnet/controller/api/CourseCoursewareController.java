package com.adinnet.controller.api;

import com.adinnet.repository.CourseCourseware;
import com.adinnet.repository.CourseLearningResource;
import com.adinnet.repository.PageBean;
import com.adinnet.repository.dto.CourseCoursewareDto;
import com.adinnet.service.CourseCoursewareService;
import com.adinnet.utils.ReturnUtil;
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

import java.util.List;
import java.util.Map;

/**
 * Created by wangx on 2018/9/21.
 */
@Controller
@RequestMapping("/api/courseCourseware")
public class CourseCoursewareController  {
    @Autowired
    private CourseCoursewareService courseCoursewareService;


    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "courseCourseware/index";
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(CourseCourseware courseCourseware) {
        ModelMap map = new ModelMap();
        map.put("queryParam", courseCourseware);
        PageBean<CourseCourseware> pageBean = courseCoursewareService.pageList3(courseCourseware);
        map.put("pageInfo", pageBean);

        return ReturnUtil.Success("加载成功", map, null);
    }
    @RequestMapping(value = "/from", method = {RequestMethod.GET})
    public String from(CourseCourseware courseCourseware, Model model) {
        if (null != courseCourseware.getId()) {
            courseCourseware = courseCoursewareService.getOne(courseCourseware.getId());
        }
        model.addAttribute("courseCourseware",courseCourseware);
        return "courseCourseware/from";
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap save(CourseCourseware courseCourseware, BindingResult result,@RequestParam(value = "pdffile",required = false) MultipartFile file) {
        if (result.hasErrors()) {
            for (ObjectError er : result.getAllErrors()) return ReturnUtil.Error(er.getDefaultMessage(), null, null);
        }
        try {
            if (null == courseCourseware.getId()) {
                return courseCoursewareService.save(courseCourseware,file);
            } else {
                return  courseCoursewareService.update(courseCourseware,file);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap  delete(Integer[] ids) {
        try {
            int id = ids[0];
            courseCoursewareService.deleteById(id);
            return ReturnUtil.Success("操作成功", null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }
}
