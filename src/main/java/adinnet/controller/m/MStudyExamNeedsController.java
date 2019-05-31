package adinnet.controller.m;

import com.adinnet.response.code.CCode;
import com.adinnet.response.json.JsonResult;
import com.adinnet.service.StudyExamNeedsService;
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
 * Created by wangx on 2018/9/19.
 * 学习须知与考试须知相关接口
 */
@RestController
@RequestMapping("m/studyExamNeeds")
public class MStudyExamNeedsController {

    @Autowired
    private StudyExamNeedsService studyExamNeedsService;

        /**
         * 查询所有须知
         * @return
         */
        @RequestMapping(value = "/allList", method = {RequestMethod.POST})
        public JsonResult needsList(HttpServletRequest request){
            try {
                String body = ServletUtils.getContent(request);
                if (StringUtils.isNotEmpty(body)) {
               JSONObject jsonObject = JSON.parseObject(body);
                    Integer type = jsonObject.getInteger("type");
                    Integer status = jsonObject.getInteger("status");
                    if (null !=type ){
                    JSONObject jo = studyExamNeedsService.queryAllNeeds(type,status);
                        return JsonResult.OK(jo);
                    }

                }
                return JsonResult.build(CCode.C_PARAM_IS_NULL);
            } catch (IOException e) {
                return JsonResult.build(CCode.C_Buz_Ex);
            }
        }


}
