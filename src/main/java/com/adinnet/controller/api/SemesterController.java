package com.adinnet.controller.api;

import com.adinnet.repository.Semester;
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

import java.util.Date;
import java.util.List;

/**
 * 学期
 * Created by RuanXiang on 2018/9/29.
 */
@Controller
@RequestMapping("/api/semester")
public class SemesterController {
    @Autowired
    private SemesterService semesterService;

    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "semester/index";
    }

    //列表
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(Semester semester) {
        ModelMap map = new ModelMap();
        Page<Semester> page = semesterService.pageList(semester);
        map.put("pageInfo", page);
        map.put("queryParam", semester);
        return ReturnUtil.Success("加载成功", map, null);
    }

    //编辑
    @RequestMapping(value = "/from", method = {RequestMethod.GET})
    public String from(Semester semester, Model model) {
        if (null != semester.getId()) {
            Integer semesterId = semester.getId();
            semester = semesterService.queryById(semesterId);
        }
        model.addAttribute("id", semester.getId());
        model.addAttribute("semester", semester);
        return "semester/from";
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public String save(Semester semester){
        try{
            String time = semester.getTime();
            time = time+" 23:59:59";
            String courseEndTime = semester.getCourseEndTime();
            courseEndTime = courseEndTime+" 23:59:59";
            semester.setTime(time);
            semester.setCourseEndTime(courseEndTime);
            if(null == semester.getId()){//保存
                semesterService.saveSemester(semester);
            }else{//更新
                semesterService.updateSemester(semester);
            }
            return "redirect:/api/semester/index";
        }catch (Exception e) {
            e.printStackTrace();
            return "";
            //return ReturnUtil.Error("操作失败", null, null);
        }
    }

    //获取是否已有本学期
    @RequestMapping(value = "getHasThisSemester")
    @ResponseBody
    public ModelMap getHasThisSemester() {
        Semester semester = semesterService.isHas();
        return ReturnUtil.Success("成功",semester);
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap delete(Integer[] ids) {
        try {
            if (ids != null) {
                if (StringUtils.isNotBlank(ids.toString())) {
                    for (Integer id : ids) {
                        semesterService.del(id);
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
}
