package com.summer.webmvc.servlet;


import java.lang.reflect.Method;
import java.util.regex.Pattern;
import lombok.Builder;
import lombok.Data;

/**
 * handlerMapping
 */
@Data
@Builder
public class SunHandlerMapping {
    private Object controller;
    private Method method;
    private Pattern pattern;
    private Boolean isResponseBody;

    @Override
    public String toString() {
        return "HandlerMapping{" +
                "controller=" + controller +
                ", method=" + method +
                ", pattern=" + pattern +
                '}';
    }

    public Object getController() {
        return controller;
    }

    public SunHandlerMapping setController(Object controller) {
        this.controller = controller;
        return this;
    }

    public Method getMethod() {
        return method;
    }

    public SunHandlerMapping setMethod(Method method) {
        this.method = method;
        return this;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public SunHandlerMapping setPattern(Pattern pattern) {
        this.pattern = pattern;
        return this;
    }
}
