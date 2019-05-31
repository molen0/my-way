package com.wonders.healthcloud.sample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class CommonController {

    @ResponseBody
    @RequestMapping("/")
    public String home() {
        return "this is my spring boot application!";
    }

    @RequestMapping("/errorpage")
    public String error() {
        return "error";
    }

    @RequestMapping("/indexpage")
    public String indexpage() {
        return "index";
    }
}
