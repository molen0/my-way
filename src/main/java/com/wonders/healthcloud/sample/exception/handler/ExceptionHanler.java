package com.wonders.healthcloud.sample.exception.handler;

import com.wonders.healthcloud.sample.exception.CommonException;
import com.wonders.healthcloud.sample.exception.ExceptionMessage;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExceptionHanler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
//        if (ex instanceof CommonException) {
//            System.out.println(((CommonException) ex).getCode() + ex.getMessage());
//        } else {
        ex.printStackTrace();
//        }
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setExtractValueFromSingleKeyModel(true);
        ModelAndView mav = new ModelAndView(view);
//        mav.addObject(new ExceptionMessage((CommonException) ex));
        return mav;
    }
}
