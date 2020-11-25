package com.summer.example.controller;

import com.summer.annotation.Autowired;
import com.summer.annotation.Controller;
import com.summer.annotation.RequestMapping;
import com.summer.example.server.Impl.ServiceBaseImpl;
import com.summer.webmvc.annotition.ResponseBody;
import com.summer.webmvc.annotition.RestController;
import com.summer.webmvc.servlet.SunModelAndView;

@RestController
public class TestController {

    @Autowired
    private ServiceBaseImpl serviceBaseImpl;

    @RequestMapping("/")
    public SunModelAndView doWork() {
        return new SunModelAndView("一个今天胜过两个明天!", null);
    }
}
