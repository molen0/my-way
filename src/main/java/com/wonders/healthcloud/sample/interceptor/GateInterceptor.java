package com.wonders.healthcloud.sample.interceptor;

import com.wonders.healthcloud.sample.enums.ExceptionEnum;
import com.wonders.healthcloud.sample.exception.CommonException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GateInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        if (request.getQueryString().equals(ExceptionEnum.NODATA.getMessage())) {
//            throw new CommonException(ExceptionEnum.NODATA);
//        }
//        if (request.getQueryString().equals(ExceptionEnum.NULL.getMessage())){
//            throw new CommonException(ExceptionEnum.NULL);
//        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
