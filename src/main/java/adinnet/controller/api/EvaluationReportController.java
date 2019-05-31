package adinnet.controller.api;

import com.adinnet.dao.EvaluateTagMapper;
import com.adinnet.repository.CourseEvaluate;
import com.adinnet.repository.EvaluateTag;
import com.adinnet.repository.Semester;
import com.adinnet.service.EvaluateTagService;
import com.adinnet.utils.ExportExcel;
import com.adinnet.utils.ReturnUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/1.
 */
@Controller
@RequestMapping("api/report")
public class EvaluationReportController {

    @Autowired
    private EvaluateTagService evaluateTagService;


    @RequestMapping(value = "/evaluateIndex", method = {RequestMethod.GET})
    public String index(Model model) {
        return "report/evaluateIndex";
    }

    @RequestMapping(value="getData",method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(EvaluateTag evaluateTag) {
        ModelMap map = new ModelMap();
        List<EvaluateTag> list = evaluateTagService.getReportList(evaluateTag);
        map.put("list",list);
        return ReturnUtil.Success("加载成功", map, null);
    }

    @RequestMapping("checkOutEvaluate")
    @ResponseBody
    public void checkOut(EvaluateTag evaluateTag,HttpServletResponse response) throws Exception {

        JSONObject jsonObject = evaluateTagService.queryAllTagList2();
        //sheet1:视频印象
        JSONArray impression = jsonObject.getJSONArray("impression");
        List<String> list = new ArrayList<>();
        List<Object[]>  dataList = new ArrayList<Object[]>();
        if(null != impression && impression.size()>0){
            for(int i=0;i<impression.size();i++){
                JSONObject result1 = impression.getJSONObject(i);
                Object[] objs = new Object[impression.size()];
                list.add(result1.getString("content"));
                objs[i] = result1.getString("nums");
                dataList.add(objs);
            }
        }
        //sheet2:期待上的课程
        List<String> list1 = new ArrayList<>();
        List<Object[]>  dataList2 = new ArrayList<Object[]>();
        JSONArray course = jsonObject.getJSONArray("course");
        if(null != course && course.size()>0){
            for(int j = 0;j<course.size();j++){
                JSONObject result2 = course.getJSONObject(j);
                Object[] objs = new Object[course.size()];
                list1.add(result2.getString("content"));
                objs[j] = result2.getString("nums");
                dataList2.add(objs);
            }
        }
        ExportExcel ex = new ExportExcel();
        Map<String,List<Object[]>> titleList = new HashMap<String,List<Object[]>>();
        titleList.put("视频印象", dataList);
        titleList.put("期待课程", dataList2);
        String[] rowsName = new String[list.size()];
        String[] rowsName1 =  new String[list1.size()];
        for(int i=0;i<list.size();i++){
            rowsName[i]=list.get(i);
        }
        for(int i=0;i<list1.size();i++){
            rowsName1[i]=list1.get(i);
        }
        List< String[]> list3 = new ArrayList<>();
        list3.add(rowsName);
        list3.add(rowsName1);
        ex.exportMoreSheet(response,titleList,list3);
    }
}
