package com.adinnet.controller.api;

import com.adinnet.repository.StudyExamNeeds;
import com.adinnet.repository.SysRole;
import com.adinnet.service.StudyExamNeedsService;
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
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wangx on 2018/9/18.
 */
@Controller
@RequestMapping("/api/studyExamNeeds")
public class StudyExamNeedsController {
    @Autowired
    private StudyExamNeedsService studyExamNeedsService;


    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "studyExamNeeds/index";
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(StudyExamNeeds studyExamNeeds) {
        ModelMap map = new ModelMap();
        Page<StudyExamNeeds> page = studyExamNeedsService.pageList(studyExamNeeds);
        map.put("pageInfo", page);
        map.put("queryParam", studyExamNeeds);
        return ReturnUtil.Success("加载成功", map, null);
    }

    @RequestMapping(value = "/from", method = {RequestMethod.GET})
    public String from(StudyExamNeeds studyExamNeeds, Model model) {
        if (null != studyExamNeeds.getId()) {
            studyExamNeeds = studyExamNeedsService.getOne(studyExamNeeds.getId());
        }
        model.addAttribute("studyExamNeeds",studyExamNeeds);
        return "studyExamNeeds/from";
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap save(StudyExamNeeds studyExamNeeds, BindingResult result) {
        if (result.hasErrors()) {
            for (ObjectError er : result.getAllErrors()) return ReturnUtil.Error(er.getDefaultMessage(), null, null);
        }
        try {
            if (null == studyExamNeeds.getId()) {
                studyExamNeedsService.save(studyExamNeeds);
            } else {
                studyExamNeedsService.update(studyExamNeeds);
            }
            return ReturnUtil.Success("操作成功", null, "/api/studyExamNeeds/index");
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
            studyExamNeedsService.deleteById(id);
            return ReturnUtil.Success("操作成功", null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }
}
