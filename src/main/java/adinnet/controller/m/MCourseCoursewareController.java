package adinnet.controller.m;

import com.adinnet.response.code.CCode;
import com.adinnet.response.json.JsonResult;
import com.adinnet.service.CourseCoursewareService;
import com.adinnet.utils.ServletUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by wangx on 2018/9/21.
 * 课件相关接口
 */
@RestController
@RequestMapping("m/courseCourseware")
public class MCourseCoursewareController {

    @Autowired
    private CourseCoursewareService courseCoursewareService;

    @RequestMapping(value = "/photoList", method = {RequestMethod.POST})
    public JsonResult outlineList(HttpServletRequest request){
        try {
            String body = ServletUtils.getContent(request);
            if (StringUtils.isNotEmpty(body)) {
                JSONObject jsonObject = JSON.parseObject(body);
              Integer  courseId  = jsonObject.getInteger("courseId");
                JSONObject jo =null;
                if (null != courseId){
                    jo = courseCoursewareService.findByCourseId(courseId);
                }
                return JsonResult.OK(jo);
            }
            return JsonResult.build(CCode.C_PARAM_IS_NULL);
        } catch (IOException e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }
}
