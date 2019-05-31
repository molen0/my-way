package adinnet.controller.api;

import com.adinnet.http.HttpUtils;
import com.adinnet.repository.CourseOutline;
import com.adinnet.utils.ReturnUtil;
import com.adinnet.utils.qiniu.QiniuUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by Administrator on 2018/9/21.
 */
@Controller
@RequestMapping("/api/upload")
@Slf4j
public class UploadController {

    @RequestMapping("qiniuUpload")
    @ResponseBody
    public ModelMap qiniuUpload(@RequestParam("file") MultipartFile file,@RequestParam("remotePath")String remotePath) throws IOException, HttpException {
        String path = QiniuUtils.upload(file,remotePath);
        return ReturnUtil.Success(path);
    }
}
