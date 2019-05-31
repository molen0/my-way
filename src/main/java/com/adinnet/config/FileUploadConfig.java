package com.adinnet.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 * Created by Administrator on 2018/9/26.
 */
/*@Configuration
public class FileUploadConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement(
            @Value("${multipart.maxFileSize}")String maxFileSize,
            @Value("${multipart.maxRequestSize}")String maxRequestSize){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大
        factory.setMaxFileSize(maxFileSize);
        //设置总上传数据大小
        factory.setMaxRequestSize(maxRequestSize);
        return factory.createMultipartConfig();
    }
}*/
