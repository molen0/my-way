package com.adinnet.controller.api;

import com.adinnet.repository.CourseOutline;
import com.adinnet.repository.ExperienceShare;
import com.adinnet.repository.PageBean;
import com.adinnet.service.ExperienceShareService;
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

@Controller
@RequestMapping("api/experienceShare")
public class ExperienceShareController {

    @Autowired
    private ExperienceShareService experienceShareService;


    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "experienceShare/index";
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(ExperienceShare experienceShare) {
        ModelMap map = new ModelMap();
        map.put("queryParam",experienceShare);
        Page<ExperienceShare> pageBean = experienceShareService.pageList(experienceShare);
        map.put("pageInfo",pageBean);
        return ReturnUtil.Success("加载成功", map, null);
    }

    @RequestMapping(value = "/from", method = {RequestMethod.GET})
    public String from(ExperienceShare experienceShare, Model model) {
        if (null != experienceShare.getId()) {
            experienceShare = experienceShareService.getOne(experienceShare.getId());
        }
        model.addAttribute("id", experienceShare.getId());
        model.addAttribute("experienceShare", experienceShare);
        return "experienceShare/from";
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap save(@RequestParam(value="photoES",required = false) MultipartFile photo,@RequestParam(value = "file",required = false) MultipartFile file,ExperienceShare experienceShare){
        try{
            if(null == experienceShare.getId()){//保存
                return experienceShareService.saveExperienceShare(experienceShare, file,photo);
            }else{//更新
                return experienceShareService.updateExperienceShare(experienceShare, file,photo);
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
                        experienceShareService.del(id);
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
