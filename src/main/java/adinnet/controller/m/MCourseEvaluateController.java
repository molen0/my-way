package adinnet.controller.m;

import com.adinnet.response.code.CCode;
import com.adinnet.response.json.JsonResult;
import com.adinnet.service.CourseEvaluateService;
import com.adinnet.utils.ServletUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by hasee on 2018/9/19.
 */
@RestController
@RequestMapping("m/courseEvaluate")
public class MCourseEvaluateController {
    @Autowired
    private CourseEvaluateService courseEvaluateService;


    /**
     * 添加评价
     * @param request
     * @return
     */
    @RequestMapping(value="saveCourseEvaluate",method = {RequestMethod.POST})
    public JsonResult saveCourseEvaluate(HttpServletRequest request){
        try {
            String body = ServletUtils.getContent(request);
            if (StringUtils.isNotEmpty(body)) {
                courseEvaluateService.saveCourseEvaluate(body);
                return JsonResult.OK();
            }
            return JsonResult.build(CCode.C_PARAM_IS_NULL);
        } catch (IOException e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }

    /**
     * 查询所有评价
     * @return
     */
    @RequestMapping(value = "/queryAllEva", method = {RequestMethod.POST})
    public JsonResult queryAllEva(HttpServletRequest request){
        try {
            String body = ServletUtils.getContent(request);
            if (StringUtils.isNotEmpty(body)) {
                return JsonResult.OK(courseEvaluateService.queryAllEva(body));
            }
            return JsonResult.build(CCode.C_PARAM_IS_NULL);
        } catch (IOException e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }
}
