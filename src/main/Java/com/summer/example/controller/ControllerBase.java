package com.summer.example.controller;

import com.summer.example.entry.User;
import com.summer.example.server.Impl.ServiceBaseImpl;
import com.summer.framework.annotation.Autowired;
import com.summer.framework.annotation.Controller;
import com.summer.framework.annotation.RequestMapping;
import com.summer.framework.webmvc.servlet.SunModelAndView;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("basepath")
public class ControllerBase {

    @Autowired
    private ServiceBaseImpl serviceBaseImpl;

    @RequestMapping("hw")
    public void hw() {
        System.out.println("Hello world");
    }

    @RequestMapping("hello")
    public User hello() {
        return serviceBaseImpl.hello();
    }

    @RequestMapping("world")
    public SunModelAndView world() {
        Map map = new HashMap<String, Object>();
        map.put("temple", serviceBaseImpl.world());
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
