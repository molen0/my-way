package adinnet.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2018/9/20.
 */
public interface QiniuUploadService {

    /**
     * 七牛文件上传
     * @param multipartFiles 文件
     * @param remoteDir 上传到七牛的文件目录
     * @return
     */
    public List<String> upload(List<MultipartFile> multipartFiles, String remoteDir) throws IOException;
}
