package com.adinnet.support.annotation;

import org.aspectj.lang.annotation.Aspect;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2018/9/18.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MRequest {

    /**
     * 是否要求登录
     *
     * @return
     */
    boolean loginRequired() default false;

    /**
     * 要求签名内容认证
     * base64(md5(content+token))
     *
     * @return
     */
    boolean validate() default false;

    String name() default "";
}
