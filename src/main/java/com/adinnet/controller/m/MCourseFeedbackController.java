package com.adinnet.controller.m;

import com.adinnet.response.code.CCode;
import com.adinnet.response.json.JsonResult;
import com.adinnet.service.CourseFeedbackService;
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
@RequestMapping("m/courseFeedback")
public class MCourseFeedbackController {

    @Autowired
    private CourseFeedbackService courseFeedbackService;


    /**
     * 添加反饋
     * @param request
     * @return
     */
    @RequestMapping(value="saveFeedBack",method = {RequestMethod.POST})
    public JsonResult saveFeedBack(HttpServletRequest request){
        try {
            String body = ServletUtils.getContent(request);
            if (StringUtils.isNotEmpty(body)) {
                courseFeedbackService.saveFeedBack(body);
                return JsonResult.OK();
            }
            return JsonResult.build(CCode.C_PARAM_IS_NULL);
        } catch (IOException e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }

  /**
     * 查询所有反饋
     * @return
     */
    @RequestMapping(value = "/queryAllFeed", method = {RequestMethod.POST})
    public JsonResult queryAllFeed(HttpServletRequest request){
        try {
            String body = ServletUtils.getContent(request);
            if (StringUtils.isNotEmpty(body)) {
                return JsonResult.OK(courseFeedbackService.queryAllFeed(body));
            }
            return JsonResult.build(CCode.C_PARAM_IS_NULL);
        } catch (IOException e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }
}
