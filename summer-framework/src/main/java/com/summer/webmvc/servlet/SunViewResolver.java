package com.summer.webmvc.servlet;

import java.io.File;
import java.util.Locale;
import java.util.Objects;
import org.eclipse.jetty.util.StringUtil;

public class SunViewResolver {

    private File rootPath;
    private static final String defaultTemple_suffx = ".html";

    public SunViewResolver(String root) {
        String templeRootPath = Objects.requireNonNull(this.getClass().getClassLoader().getResource(root)).getFile();
        rootPath = new File(templeRootPath);
    }

    public SunView resolveViewName(String viewName, Locale locale) {
        if (StringUtil.isBlank(viewName)) {
            return null;
        }
        viewName = viewName.toLowerCase().endsWith(defaultTemple_suffx) ? viewName : (viewName + defaultTemple_suffx);
        File templeFile = new File((rootPath.getPath() + "/" + viewName).replace("/+", "/"));
        return new SunView(templeFile);
    }
}
