package com.summer.framework.webmvc.servlet;

import java.io.File;
import java.util.Locale;

public class SunViewResolver {

    private File rootPath;
    private static final String defaultTemple_suffx = ".html";

    public SunViewResolver(String root) {
        String templeRootPath = this.getClass().getClassLoader().getResource(root).getFile();
        rootPath = new File(templeRootPath);
    }

    public SunErrorView resolveViewName(String viewName, Locale locale) {
        if (viewName == null || viewName == "") {
            return null;
        }
        viewName = viewName.toLowerCase().endsWith(defaultTemple_suffx) ? viewName : (viewName + defaultTemple_suffx);
        File templeFile = new File((rootPath.getPath() + "/" + viewName).replace("/+", "/"));
        return new SunErrorView(templeFile);
    }
}
