package adinnet.controller.api;

import com.adinnet.repository.Specialist;
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

/**
 * 专家
 * Created by RuanXiang on 2018/9/30.
 */
@Controller
@RequestMapping("/api/specialist")
public class SpecialistController {

    @Autowired
    private SpecialistService specialistService;

    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "specialist/index";
    }

    //列表
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(Specialist specialist) {
        ModelMap map = new ModelMap();
        Page<Specialist> page = specialistService.pageList(specialist);
        map.put("pageInfo", page);
        map.put("queryParam", specialist);
        return ReturnUtil.Success("加载成功", map, null);
    }

    //编辑
    @RequestMapping(value = "/from", method = {RequestMethod.GET})
    public String from(Specialist specialist, Model model) {
        if (null != specialist.getId()) {
            specialist = specialistService.queryById(specialist.getId());
        }
        model.addAttribute("id", specialist.getId());
        model.addAttribute("specialist", specialist);
        return "specialist/from";
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public String save(@RequestParam("photoU") MultipartFile file, Specialist specialist){
        try{
            if(null == specialist.getId()){//保存
                specialistService.saveSpecialist(specialist,file);
            }else{//更新
                specialistService.updateSpecialist(specialist, file);
            }
            return "redirect:/api/specialist/index";
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
                        specialistService.del(id);
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
