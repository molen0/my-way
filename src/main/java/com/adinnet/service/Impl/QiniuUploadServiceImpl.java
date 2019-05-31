package com.adinnet.service.Impl;

import com.adinnet.common.QiniuConfig;
import com.adinnet.service.QiniuUploadService;
import com.adinnet.utils.qiniu.QiniuUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/20.
 */

@Service
public class QiniuUploadServiceImpl implements QiniuUploadService {

    @Override
    public List<String> upload(List<MultipartFile> multipartFiles, String remoteDir) throws IOException {
        List<String> list = new ArrayList<>();
        for (MultipartFile multipartFile:multipartFiles){
           String path =  QiniuUtils.upload(multipartFile,remoteDir);
            list.add(path);
        }
        return list;
    }
}
