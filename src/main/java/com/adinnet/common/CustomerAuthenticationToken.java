/*
 * Copyright (c) 2017-2018.  放牛极客<l_iupeiyu@qq.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * </p>
 *
 */

package com.adinnet.common;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * author wangren
 * date 2018/09/19
 */
public class CustomerAuthenticationToken extends UsernamePasswordToken {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String captcha;
    /**
     * 用来区分前后台登录的标记
     */
    private String loginType;
    /**
     * 用来区分登录用户的渠道
     */
    private String loginForm;

    private String token;

    public CustomerAuthenticationToken(String username, String password, boolean rememberMe) {
        super(username, password, rememberMe);
    }

    public CustomerAuthenticationToken(String token, String loginType){
        this.token = token;
        this.loginType = loginType;
    }



    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getLoginForm() {
        return loginForm;
    }

    public void setLoginForm(String loginForm) {
        this.loginForm = loginForm;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        if(this.token != null){
            return this.token;
        }else{
            return this.getUsername();
        }

    }

    @Override
    public Object getCredentials() {
        if(this.token != null){
            return this.token;
        }else{
            return this.getPassword();
        }
    }
}
