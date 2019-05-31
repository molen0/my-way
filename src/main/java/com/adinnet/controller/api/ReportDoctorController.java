package com.adinnet.controller.api;

import com.adinnet.repository.*;
import com.adinnet.repository.vo.ReportVo;
import com.adinnet.service.ReportDoctorService;
import com.adinnet.service.SemesterService;
import com.adinnet.utils.ExportExcel;
import com.adinnet.utils.ReturnUtil;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/10/31.
 */
@Controller
@RequestMapping("/api/reportDoctor")
public class ReportDoctorController {
    @Autowired
    private ReportDoctorService reportDoctorService;

    @Autowired
    private SemesterService semesterService;


    @RequestMapping(value = "/doctorIndex", method = {RequestMethod.GET})
    public String index(Model model) {
        return "reportDoctor/index";
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(ReportDoctor reportDoctor) {
        ModelMap map = new ModelMap();
        map.put("queryParam", reportDoctor);
        PageBean<ReportDoctor> pageBean = reportDoctorService.pageList(reportDoctor);
        map.put("pageInfo", pageBean);

        return ReturnUtil.Success("加载成功", map, null);
    }

    /**
     * 各区医生学分记录导出
     * @param
     * @param response
     */
    @RequestMapping(value = "/putOut")
    @ResponseBody
    public void checkOut(@Param("name") String name,@Param("area") String area, @Param("credit") String credit, @Param("sdId") Integer sdId, HttpServletResponse response) throws Exception {

        ReportDoctor reportDoctor = new ReportDoctor();
        if (null !=name && !"".equals(name)){
            reportDoctor.setName(name);
        }
        if (null !=area && !"".equals(area) &&  !"null".equals(area)){
            reportDoctor.setArea(area);
        }
        if (null !=credit && !"".equals(credit) &&  !"null".equals(credit)){
            reportDoctor.setCredit(credit);
        }
        if (null !=sdId && !"".equals(sdId) &&  !"null".equals(sdId)){
            reportDoctor.setSdId(sdId);
        }

        String[] rowsName = new String[]{"手机号码","医生属性","职称","年龄","所属区","姓名","性别","单位","学分","邮箱"};
        PageBean<ReportDoctor> pageBean = reportDoctorService.page(reportDoctor);
        List<ReportDoctor> list = pageBean.getDatas();
        List<Object[]>  dataList = new ArrayList<Object[]>();
        for (int i = 0; i < list.size(); i++) {
            ReportDoctor man = list.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = (null ==man.getPhone()? "--":man.getPhone());
            objs[1] = (1==man.getProperty()? "全科医生":"乡村医生");
            objs[2] = (null ==man.getTitle()? "--":man.getTitle());
            objs[3] =(null ==man.getAge()? "--":man.getAge()); ;
            objs[4] = (null ==man.getAreaName()? "--":man.getAreaName());
            objs[5] = (null ==man.getName()? "--":man.getName());
            objs[6] = ("1".equals(man.getGender())? "男":"女");
            objs[7] = (null ==man.getCompany()? "--":man.getCompany());
            objs[8] = (null ==man.getCredit()? "--":man.getCredit());
            objs[9] = (null ==man.getEmail()? "--":man.getEmail());
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel("各区域医生学分统计", rowsName, dataList);
        ex.export(response);
    }

    @RequestMapping(value = "getArea")
    @ResponseBody
    public ModelMap getCourse() {
        List<DoctorArea> list = reportDoctorService.findAllArea();
        return ReturnUtil.Success("成功",list);
    }
    @RequestMapping(value = "getSemester")
    @ResponseBody
    public ModelMap getSemester() {
        ModelMap map = new ModelMap();
        Semester semester = semesterService.isHas();
        Integer semesterId =null;
        if (null !=semester) {
            // 当前学期id
           semesterId = semester.getId();
        }
        List<Semester> list = semesterService.queryAllSemester();
        map.put("list",list);
        map.put("semesterId",semesterId);
        return ReturnUtil.Success("成功",map);
    }
}
