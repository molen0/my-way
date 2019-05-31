package com.adinnet.controller.api;

import com.adinnet.utils.ReturnUtil;
import com.adinnet.utils.qiniu.QiniuUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping("/api/ueditor")
public class UeditorController {

    @Value("${ueditor.config.json.path}")
    private String configJSONPath;

    @RequestMapping("/save")
    @ResponseBody
    public ModelMap save(String content){
        return ReturnUtil.Success(content);
    }


    @RequestMapping
    @ResponseBody
    public void  index(@RequestParam("action") String action, HttpServletRequest request, HttpServletResponse response){
        try {
            PrintWriter writer = response.getWriter();
            if(action.equals("config")){//返回配置文件
                InputStream stream = getClass().getClassLoader().getResourceAsStream(configJSONPath);
                File targetFile = new File("config.json");
                FileUtils.copyInputStreamToFile(stream, targetFile);
                writer.write(FileUtils.readFileToString(targetFile,"utf-8"));
            }else if(action.equals("uploadimage") || action.equals("uploadscrawl") || action.equals("uploadvideo") || action.equals("uploadfile")) {//上传文件
                Map<String,Object> mp = new HashMap<String, Object>();
                CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
                //判断 request 是否有文件上传,即多部分请求
                if(multipartResolver.isMultipart(request)){
                    //转换成多部分request
                    MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
                    //取得request中的所有文件名
                    Iterator<String> iter = multiRequest.getFileNames();
                    while(iter.hasNext()){
                        //取得上传文件
                        MultipartFile file = multiRequest.getFile(iter.next());
                        String remotePath = "ueditor";
                        String path = QiniuUtils.upload(file,remotePath);
                        mp.put("state","SUCCESS");
                        mp.put("url",path);
                        writer.print(JSONObject.toJSON(mp));
                    }
                }
            }
            writer.flush();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
