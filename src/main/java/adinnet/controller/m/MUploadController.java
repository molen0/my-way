package adinnet.controller.m;

import com.adinnet.response.code.CCode;
import com.adinnet.response.json.JsonResult;
import com.adinnet.utils.ServletUtils;
import com.adinnet.utils.qiniu.QiniuUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2018/9/26.
 */
@RestController
@RequestMapping("m/upload")
@Slf4j
public class MUploadController {

    @RequestMapping(value="/uploadFiles",method = {RequestMethod.POST})
    public JsonResult outlineList(HttpServletRequest request,@RequestParam("pictures")MultipartFile[] files){
        try {
            List<String> pathList =  QiniuUtils.uploadMore(files,"picture");
            log.info("----------------------返回的图片集合为："+pathList);
            return JsonResult.OK(pathList);
        } catch (IOException e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }
}
