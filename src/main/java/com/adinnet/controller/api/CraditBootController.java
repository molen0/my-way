package com.adinnet.controller.api;

import com.adinnet.repository.CreaditBook;
import com.adinnet.repository.ExaminUser;
import com.adinnet.repository.Semester;
import com.adinnet.repository.SysRole;
import com.adinnet.service.CreaditBookService;
import com.adinnet.service.SemesterService;
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

import java.util.List;
import java.util.Map;

/**
 * @author wangren
 * @Description: 证书模板
 * @create 2018-10-09 15:04
 **/
@Controller
@RequestMapping("/api/craditBoot")
public class CraditBootController {

    @Autowired
    private CreaditBookService creaditBookService;
    @Autowired
    private SemesterService semesterService;
    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String creditIndex(Model model) {
        return "creditBook/index";
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(CreaditBook creaditBook) {
        try{
            ModelMap map = new ModelMap();
            map.put("queryParam",creaditBook);
            Page<CreaditBook> page = creaditBookService.pageList(creaditBook);
            map.put("pageInfo",page);
            return ReturnUtil.Success("加载成功", map, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("加载失败", null, null);
        }
    }
    @RequestMapping(value = "/from", method = {RequestMethod.GET})
    public String from(CreaditBook creaditBook, Model model) {
       // List<Semester> list = semesterService.queryCreaditBookOthers();
        if (null != creaditBook.getId()) {
            creaditBook = creaditBookService.getOne(creaditBook.getId());
           // list.add(creaditBook.getSemester());
        }
        //model.addAttribute("modes",list);
        model.addAttribute("creaditBook", creaditBook);
        return "creditBook/from";
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap save( CreaditBook creaditBook, BindingResult result) {
        if (result.hasErrors()) {
            for (ObjectError er : result.getAllErrors()) return ReturnUtil.Error(er.getDefaultMessage(), null, null);
        }
        try {
            creaditBookService.saveOrUpdate(creaditBook);
            return ReturnUtil.Success("操作成功", null, "/api/craditBoot/index");
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap delete(Integer id) {
        try {
            if (null == id) {
                return ReturnUtil.Error("Error", null, null);
            } else {
                creaditBookService.del(id);
                return ReturnUtil.Success("操作成功", null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }
}
