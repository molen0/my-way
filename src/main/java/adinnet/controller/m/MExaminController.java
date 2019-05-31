package adinnet.controller.m;

import com.adinnet.repository.Course;
import com.adinnet.repository.ExaminUser;
import com.adinnet.repository.Semester;
import com.adinnet.repository.vo.AnswerVo;
import com.adinnet.repository.vo.ExaminUserVo;
import com.adinnet.response.code.CCode;
import com.adinnet.response.json.JsonResult;
import com.adinnet.service.CourseService;
import com.adinnet.service.ExaminUserService;
import com.adinnet.service.SemesterService;
import com.adinnet.utils.ServletUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wangren
 * @Description: 考试管理
 * @create 2018-09-29 15:45
 **/
@RestController
@RequestMapping("m/examin")
public class MExaminController {

    @Autowired
    private ExaminUserService examinUserService;
    @Autowired
    private CourseService courseService;
    @Autowired
    SemesterService semesterService;
    @RequestMapping(value = "/isflag", method = {RequestMethod.GET})
    public JsonResult isflag(Integer courseId,String userId){
        try {
            if(null != courseId && null != userId && !"".equals(userId)) {
            List<ExaminUser> examinUsers = examinUserService.selectUser(courseId,userId);
            Course course = courseService.getOne(courseId);
            Semester semester = semesterService.queryByCourseId(courseId);
                SimpleDateFormat sdf =  new SimpleDateFormat("yyyy/MM/dd");
                SimpleDateFormat sdf1 =  new SimpleDateFormat("yyyy-MM-dd");
                Map<String, Object> map = new HashedMap();
                Date date2 = sdf1.parse(semester.getCourseEndTime());
                String dateStr = sdf.format(date2);
                String dateStr1 = sdf.format(new Date());
                Date date = sdf.parse(dateStr);
                Date date1 = sdf.parse(dateStr1);
                map.put("date",dateStr);
                if(date1.before(date)){
                    map.put("isExaminflag",0);
                }else {
                    map.put("isExaminflag", 1);
                }
            if(null != course && "1".equals(course.getCourseType().toString())){
                if(examinUsers.size()>=2){
                    map.put("isflag",1);
                }else{
                    map.put("isflag",0);
                }
            }else{
                if(examinUsers.size()>0){
                    map.put("isflag",1);
                }else{
                    map.put("isflag",0);
                }
            }
            return JsonResult.OK(map);
            }
            return JsonResult.build(CCode.C_PARAM_EX);
        } catch (Exception e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public JsonResult list(Integer courseId){
        try {
            if(null != courseId ) {
            return JsonResult.OK(examinUserService.selectExamin(courseId));
            }
            return JsonResult.build(CCode.C_PARAM_EX);
        } catch (Exception e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public JsonResult save(HttpServletRequest request){
        try {
            String body = ServletUtils.getContent(request);
            JSONObject jsonObject = JSON.parseObject(body);
            Integer id = jsonObject.getInteger("id");
            String userId = jsonObject.getString("userId");
            JSONArray jsonArray = jsonObject.getJSONArray("answerList");
            if(null != id && null != userId && !"".equals(userId) ) {
                String str = JSONObject.toJSONString(jsonArray, SerializerFeature.WriteClassName);
                List<AnswerVo> list = JSONObject.parseArray(str, AnswerVo.class);
                ExaminUserVo examinUserVo = new ExaminUserVo();
                examinUserVo.setId(id);
                examinUserVo.setUserId(userId);
                examinUserVo.setAnswerList(list);
                return JsonResult.OK(examinUserService.saveExaminUserVo(examinUserVo,jsonArray));
            }
            return JsonResult.build(CCode.C_PARAM_EX);
        } catch (Exception e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }

    @RequestMapping(value = "/checkDate", method = {RequestMethod.GET})
    public JsonResult checkDate(Integer courseId,String userId){
        try {
            if(null != courseId && null != userId && !"".equals(userId)) {
                return JsonResult.OK(examinUserService.checkDate(courseId, userId));
            }
            return JsonResult.build(CCode.C_PARAM_EX);
        } catch (Exception e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }

    @RequestMapping(value = "/wrongset", method = {RequestMethod.GET})
    public JsonResult wrongset(Integer courseId,String userId){
        try {
            if(null != courseId && null != userId && !"".equals(userId)){
                return JsonResult.OK(examinUserService.wrongset(courseId,userId));
            }
            return JsonResult.build(CCode.C_PARAM_EX);
        } catch (Exception e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }
    @RequestMapping(value = "/cradit", method = {RequestMethod.GET})
    public JsonResult cradit(Integer courseId,Integer userId){
        try {
            if(null != courseId && null != userId && !"".equals(userId)){
                return JsonResult.OK(examinUserService.selectCradit(courseId,userId));
            }
            return JsonResult.build(CCode.C_PARAM_EX);
        } catch (Exception e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }
}
