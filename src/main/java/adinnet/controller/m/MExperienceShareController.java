package adinnet.controller.m;

import com.adinnet.response.code.CCode;
import com.adinnet.response.json.JsonResult;
import com.adinnet.service.ExperienceShareService;
import com.adinnet.utils.ServletUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2018/10/10.
 */
@RestController
@RequestMapping("m/experienceShare")
public class MExperienceShareController {

    @Autowired
    private ExperienceShareService experienceShareService;

    /**
     * 获取经验分享
     * @param request
     * @return
     */
    @RequestMapping(value="index", method = {RequestMethod.POST})
    public JsonResult index(HttpServletRequest request){
        try {
            String body = ServletUtils.getContent(request);
            JSONObject jsonObject = JSON.parseObject(body);
            Integer doctorId = jsonObject.getInteger("doctorId");
            if(doctorId != null){
                Map map = experienceShareService.queryExperienceShare(doctorId);
                if(null !=map.get("code")){
                    return JsonResult.build(CCode.C_DOCTOR_IS_NULL);
                }
                return JsonResult.OK(map);
            }
            return JsonResult.build(CCode.C_PARAM_IS_NULL);
        } catch (IOException e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }

    /**
     * 根据类型获取经验分享
     * @param request
     * @return
     */
    @RequestMapping(value="getList",method = {RequestMethod.POST})
    public JsonResult getList(HttpServletRequest request){
        try {
            String body = ServletUtils.getContent(request);
            JSONObject jsonObject = JSON.parseObject(body);
            Integer doctorId = jsonObject.getInteger("doctorId");
            Integer type = jsonObject.getInteger("type");
            if(doctorId != null && type != null){
                Map map = experienceShareService.getListByType(doctorId, type);
                if(null !=map.get("code")){
                    return JsonResult.build(CCode.C_DOCTOR_IS_NULL);
                }
                return JsonResult.OK(map);
            }
            return JsonResult.build(CCode.C_PARAM_IS_NULL);
        } catch (IOException e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }

    /**
     * 获取经验分享详情
     * @param request
     * @return
     */
    @RequestMapping(value = "getDetail",method = {RequestMethod.POST})
    public JsonResult getDetail(HttpServletRequest request){
        try {
            String body = ServletUtils.getContent(request);
            JSONObject jsonObject = JSON.parseObject(body);
            Integer doctorId = jsonObject.getInteger("doctorId");
            Integer id = jsonObject.getInteger("id");
            if(doctorId != null && id != null){
                Map map = experienceShareService.getDetail(doctorId, id);
                if(null !=map.get("code")){
                    return JsonResult.build(CCode.C_DOCTOR_IS_NULL);
                }
                return JsonResult.OK(map);
            }
            return JsonResult.build(CCode.C_PARAM_IS_NULL);
        } catch (IOException e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }
}
