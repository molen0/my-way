package com.wonders.healthcloud.sample.config;

import com.wonders.healthcloud.sample.exception.handler.ExceptionHanler;
import com.wonders.healthcloud.sample.interceptor.GateInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GateInterceptor()).addPathPatterns("/**");
    }

    @Override
    protected void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new ExceptionHanler());
    }
}
