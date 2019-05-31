package com.adinnet.controller.api;

import com.adinnet.repository.Doctor;
import com.adinnet.service.DoctorService;
import com.adinnet.utils.ReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 在线报名用户
 * Created by RuanXiang on 2018/9/26.
 */
@Controller
@RequestMapping("/api/doctor")
public class OnlineSignUpController {
    @Autowired
    private DoctorService doctorService;

    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "doctor/index";
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(Doctor doctor) {
        ModelMap map = new ModelMap();
        Page<Doctor> page = doctorService.pageList(doctor);
        map.put("pageInfo", page);
        map.put("queryParam", doctor);
        return ReturnUtil.Success("加载成功", map, null);
    }
}
