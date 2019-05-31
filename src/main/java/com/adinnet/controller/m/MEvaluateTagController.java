package com.adinnet.controller.m;

import com.adinnet.response.code.CCode;
import com.adinnet.response.json.JsonResult;
import com.adinnet.service.EvaluateTagService;
import com.adinnet.utils.ServletUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by hasee on 2018/9/18.
 */
@RestController
@RequestMapping("m/evaluateTag")
public class MEvaluateTagController {
    @Autowired
    private EvaluateTagService evaluateTagService;

    /**
     * 查询所有评价标签
     * @return
     */
    @RequestMapping(value = "/evaluateTagList", method = {RequestMethod.POST})
    public JsonResult outlineList(HttpServletRequest request){
        try {
            String body = ServletUtils.getContent(request);
            if (StringUtils.isNotEmpty(body)) {
                 JSONObject jo = evaluateTagService.queryAllTagList();
                 return JsonResult.OK(jo);
             }
            return JsonResult.build(CCode.C_PARAM_IS_NULL);
        } catch (IOException e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }



}
