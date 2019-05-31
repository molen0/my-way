package com.adinnet.controller.api;

import com.adinnet.repository.EvaluateTag;
import com.adinnet.service.EvaluateTagService;
import com.adinnet.utils.ReturnUtil;
import lombok.extern.slf4j.Slf4j;
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

/**
 * Created by hasee on 2018/9/18.
 */
@Controller
@RequestMapping("/api/evaluateTag")
@Slf4j
public class EvaluateTagController {
    @Autowired
    private EvaluateTagService evaluateTagService;


    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "evaluateTag/index";
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(EvaluateTag evaluateTag) {
        ModelMap map = new ModelMap();
        Page<EvaluateTag> page = evaluateTagService.pageList(evaluateTag);
        map.put("pageInfo", page);
        map.put("queryParam", evaluateTag);
        return ReturnUtil.Success("加载成功", map, null);
    }



    @RequestMapping(value = "/from", method = {RequestMethod.GET})
    public String from(EvaluateTag evaluateTag, Model model) {
        if (null != evaluateTag.getId()) {
            evaluateTag = evaluateTagService.getOne(evaluateTag.getId());
        }
        model.addAttribute("id", evaluateTag.getId());
        model.addAttribute("evaluateTag", evaluateTag);
        return "evaluateTag/from";
    }


    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap save( EvaluateTag evaluateTag, BindingResult result) {
        if (result.hasErrors()) {
            for (ObjectError er : result.getAllErrors()) return ReturnUtil.Error(er.getDefaultMessage(), null, null);
        }
        try {
            if (null == evaluateTag.getId()) {
                evaluateTagService.saveEvaluateTag(evaluateTag);
            } else {
                evaluateTagService.updateEvaluateTag(evaluateTag);
            }
            return ReturnUtil.Success("操作成功", null, "/api/evaluateTag/index");
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
                        evaluateTagService.del(id);
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
