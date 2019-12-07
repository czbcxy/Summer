package com.summer.framework.webmvc.servlet;

import lombok.Data;

import java.util.Map;

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
