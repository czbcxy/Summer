package com.summer.example;

import com.summer.framework.annotation.Autowired;
import com.summer.framework.annotation.Controller;
import com.summer.framework.annotation.RequestMapping;
import com.summer.framework.webmvc.servlet.SunModelAndView;
import sun.rmi.runtime.Log;

import java.util.HashMap;

@Controller
@RequestMapping("basepath")
public class ControllerDemo {

    @Autowired
    private ServiceDemo serviceDemo;

    @RequestMapping("hello")
    public void hello() {
        serviceDemo.test();
    }

    @RequestMapping("index")
    public SunModelAndView index() {
        HashMap<String, Object> model = new HashMap<>();
        return new SunModelAndView("index", model);
    }

    @RequestMapping("do404")
    public SunModelAndView work404() {
        HashMap<String, Object> model = new HashMap<>();
        return new SunModelAndView("404", model);
    }

    @RequestMapping("do500")
    public SunModelAndView work500() {
        HashMap<String, Object> model = new HashMap<>();
        return new SunModelAndView("500", model);
    }

}
