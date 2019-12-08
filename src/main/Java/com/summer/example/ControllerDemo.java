package com.summer.example;

import com.summer.framework.annotation.Autowired;
import com.summer.framework.annotation.Controller;
import com.summer.framework.annotation.RequestMapping;
import com.summer.framework.webmvc.servlet.SunModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("basepath")
public class ControllerDemo {

    @Autowired
    private ServiceDemo serviceDemo;

    @RequestMapping("hw")
    public void hw() {
        System.out.println("Hello world");
    }

    @RequestMapping("hello")
    public String hello() {
        return serviceDemo.hello();
    }

    @RequestMapping("world")
    public SunModelAndView world() {
        Map map = new HashMap<String, Object>();
        map.put("temple", serviceDemo.world());
        return new SunModelAndView("temple", map);
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
        String[] str = null;
        for (String s : str) {
            System.out.println(s);
        }
        return null;
    }

}
