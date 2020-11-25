package com.summer.webmvc.servlet;

import java.util.Map;
import lombok.Data;

@Data
public class SunModelAndView {
    private String viewName;
    private Map<String,?> model;

    public SunModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public SunModelAndView() {
    }

    public SunModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }
}
