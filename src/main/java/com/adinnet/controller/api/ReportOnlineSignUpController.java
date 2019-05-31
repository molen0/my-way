package com.adinnet.controller.api;

import com.adinnet.repository.Semester;
import com.adinnet.repository.vo.ReportOnlineSignUpVo;
import com.adinnet.service.ReportService;
import com.adinnet.service.SemesterService;
import com.adinnet.utils.ExportExcel;
import com.adinnet.utils.ReturnUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 报名信息报表
 * Created by RuanXiang on 2018/11/7.
 */
@Controller
@RequestMapping("/api/reportOnlineSignUp")
@Slf4j
public class ReportOnlineSignUpController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private SemesterService semesterService;

    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        model.addAttribute("queryPama",semesterService.selectAllList());
        return "reportOnlineSignUp/index";
    }

    //列表
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(Integer semesterId,Integer courseAttr) {
        ModelMap map = new ModelMap();
        List<ReportOnlineSignUpVo> list = reportService.getSignUpAndCourseList(semesterId,courseAttr);
        map.put("pageInfo", list);
        //map.put("queryParam", semester);
        return ReturnUtil.Success("加载成功", map, null);
    }

    @RequestMapping(value = "/checkOut")
    @ResponseBody
    public void checkOut(Semester semester, HttpServletResponse response, Integer property) throws Exception {
        String[] rowsName = new String[]{"区域","在编总人数","报名人数","学习人数","考试人数","培训率","考试率","完成全部课程人数","全部课程完成率"};
        List<ReportOnlineSignUpVo> list = reportService.getSignUpAndCourseList(semester.getId(),property);
        semester = reportService.getSemester(semester);
        List<Object[]>  dataList = new ArrayList<Object[]>();
        for (int i = 0; i < list.size(); i++) {
            ReportOnlineSignUpVo man = list.get(i);
            Object[] objs = new Object[rowsName.length];
            objs[0] = man.getAreaName();
            objs[1] = man.getInCount();
            objs[2] = man.getSignUpCount();
            objs[3] = man.getLearnCount();
            objs[4] = man.getStudyCount();
            objs[5] = man.getPercentLearnCount();
            objs[6] = man.getPercentStudyCount();
            objs[7] = man.getCourseCount();
            objs[8] = man.getPercentCourseCount();
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(semester.getName()+(property==0?"统计分析——乡村医生":"统计分析——全科医生"), rowsName, dataList);
        ex.export(response);
    }
}
