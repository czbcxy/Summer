package com.summer.example.controller;

import com.summer.annotation.Autowired;
import com.summer.annotation.Controller;
import com.summer.annotation.RequestMapping;
import com.summer.example.entry.User;
import com.summer.example.server.Impl.ServiceBaseImpl;
import com.summer.webmvc.annotition.ResponseBody;
import com.summer.webmvc.servlet.SunModelAndView;
import java.util.HashMap;
import java.util.Map;

@Controller
//@ResponseBody
@RequestMapping("basepath")
public class ControllerBase {

    @Autowired
    private ServiceBaseImpl serviceBaseImpl;

    @RequestMapping("bd")
    public SunModelAndView bd() {
        System.out.println("Hello world");
        HashMap<String, Object> map = new HashMap<>();
        map.put("baidu", "http://www.baidu.com");
        return new SunModelAndView("temple", map);
    }

    @RequestMapping("motto")
    @ResponseBody
    public User motto() {
        return serviceBaseImpl.motto();
    }

    @RequestMapping("summer")
    public SunModelAndView world() {
        Map<String, Object> map = new HashMap<>();
        map.put("summer", serviceBaseImpl.world());
        return new SunModelAndView("temple", map);
    }

    @RequestMapping("index")
    public SunModelAndView index() {
        return new SunModelAndView("index", new HashMap<>());
    }

    @RequestMapping("do404")
    public SunModelAndView work404() {
        return new SunModelAndView("404", null);
    }

    @RequestMapping("do500")
    public SunModelAndView work500() {
        int x = 0 / 0;
        return null;
    }
}
