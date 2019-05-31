package com.adinnet.controller.api;

import com.adinnet.repository.Menu;
import com.adinnet.repository.Semester;
import com.adinnet.repository.vo.ReportVo;
import com.adinnet.service.ReportService;
import com.adinnet.service.SemesterService;
import com.adinnet.utils.ExportExcel;
import com.adinnet.utils.ReturnUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
 * @author wangren
 * @Description: 报表学分管理
 * @create 2018-10-31 14:27
 **/
@Controller
@RequestMapping("/api/report")
@Slf4j
public class ReportController {

    @Autowired
    private  SemesterService semesterService;

    @Autowired
    private ReportService reportService;

    @RequestMapping(value = "/generalIndex", method = {RequestMethod.GET})
    public String generalIndex(Model model) {
        model.addAttribute("queryPama",semesterService.selectAllList());
        return "report/generalIndex";
    }

    @RequestMapping(value = "/ruralIndex", method = {RequestMethod.GET})
    public String ruralIndex(Model model) {
        model.addAttribute("queryPama",semesterService.selectAllList());
        return "report/ruralIndex";
    }

    @RequestMapping(value = "/generalList", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap generalList(Semester semester) {
        ModelMap map = new ModelMap();
        List<ReportVo> list = reportService.generalList(semester,1);
        map.put("pageInfo", list);
        map.put("queryParam", semester);
        return ReturnUtil.Success("加载成功", map, null);
    }

    @RequestMapping(value = "/ruralList", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap ruralList(Semester semester) {
        ModelMap map = new ModelMap();
        List<ReportVo> list = reportService.generalList(semester,0);
        map.put("pageInfo", list);
        map.put("queryParam", semester);
        return ReturnUtil.Success("加载成功", map, null);
    }
    @RequestMapping(value = "/checkOut")
    @ResponseBody
    public void checkOut(Semester semester,HttpServletResponse response,Integer property) throws Exception {
        String[] rowsName = new String[]{"","报名人数","得5分人数","得5分人数占比","得3-4.5分人数","得3-4.5分人数占比","得0.5-2.5分人数","得0.5-2.5分人数占比","无学分人数","无学分人数占比"};
        List<ReportVo> list = reportService.generalList(semester,property);
        semester = reportService.getSemester(semester);
        List<Object[]>  dataList = new ArrayList<Object[]>();
        for (int i = 0; i < list.size(); i++) {
            ReportVo man = list.get(i);
            Object[] objs = new Object[rowsName.length];
            ;
            objs[0] = (null ==man.getName()? "--":man.getName());
            objs[1] = (null ==man.getCountAll()? "--":man.getCountAll());
            objs[2] = (null ==man.getCount5()? "--":man.getCount5());
            objs[3] = (null ==man.getPercent5()? "--":man.getPercent5());
            objs[4] = (null ==man.getCount3()? "--":man.getCount3());
            objs[5] = (null ==man.getPercent3()? "--":man.getPercent3());
            objs[6] = (null ==man.getCount1()? "--":man.getCount1());
            objs[7] =(null == man.getPercent1()? "--": man.getPercent1());
            objs[8] = (null ==man.getCount0()? "--": man.getCount0());
            objs[9] = (null ==man.getPercent0() ? "--": man.getPercent0());
            dataList.add(objs);
        }
        ExportExcel ex = new ExportExcel(semester.getName(), rowsName, dataList);
        ex.export(response);
    }
}
