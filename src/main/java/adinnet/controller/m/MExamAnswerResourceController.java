package adinnet.controller.m;

import com.adinnet.repository.ExamAnswerResource;
import com.adinnet.response.code.CCode;
import com.adinnet.response.json.JsonResult;
import com.adinnet.service.ExamAnswerResourceService;
import com.adinnet.utils.ReturnUtil;
import com.adinnet.utils.ServletUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2018/10/10.
 * 参考答案相关接口
 */
@RestController
@RequestMapping("/m/answer")
public class MExamAnswerResourceController {


    @Autowired
    private ExamAnswerResourceService examAnswerResourceService;


    @RequestMapping(value = "/byCourseId", method = {RequestMethod.POST})
    public JsonResult findByCourseId(HttpServletRequest request) {
        try {
            String body = ServletUtils.getContent(request);
            if (StringUtils.isNotEmpty(body)) {
                JSONObject jsonObject = JSONObject.parseObject(body);
                Integer courseId = jsonObject.getInteger("courseId");
                JSONObject jo = new JSONObject();
                if (null != courseId) {
                   jo = examAnswerResourceService.findByCourseId(courseId);

                }
                return JsonResult.OK(jo);
            }
            return JsonResult.build(CCode.C_PARAM_IS_NULL);
        } catch (IOException e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }
}
