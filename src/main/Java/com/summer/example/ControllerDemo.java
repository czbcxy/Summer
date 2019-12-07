package com.summer.example;

import com.summer.framework.annotation.Autowired;
import com.summer.framework.annotation.Controller;
import com.summer.framework.annotation.RequestMapping;

@Controller
@RequestMapping("basepath")
public class ControllerDemo {

    @Autowired
    private ServiceDemo serviceDemo;

    @RequestMapping("hello")
    public String hello() {
        serviceDemo.test();
        return "Hello world";
    }
}
