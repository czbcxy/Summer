package com.summer.framework.webmvc.servlet;

import com.summer.framework.webmvc.annotition.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * adapter
 */
public class SunHandlerAdapter {

    public boolean supports(Object handler) {
        return handler instanceof SunHandlerMapping;
    }

    public SunModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SunHandlerMapping handlerMapping = (SunHandlerMapping) handler;
        Object controller = handlerMapping.getController();
        Method method = handlerMapping.getMethod();
        //参数匹配
        Map<String, Integer> paramsIndexMapping = new HashMap<>();

        //获取所有参数注解列表@RequestParam
        Annotation[][] pa = method.getParameterAnnotations();
        for (int x = 0; x < pa.length; x++) {
            for (Annotation a : pa[x]) {
                if (a instanceof RequestParam) {
                    String paramName = ((RequestParam) a).name();
                    if (!"".equals(paramName.trim())) {
                        paramsIndexMapping.put(paramName, x);
                    }
                }
            }
        }

        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> type = parameterTypes[i];
            if (type == HttpServletRequest.class || type == HttpServletResponse.class) {
                paramsIndexMapping.put(type.getName(), i);
            }
        }


        Map<String, String[]> parameterMap = request.getParameterMap();
        Object[] paramValues = new Object[parameterTypes.length];
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String value = Arrays.toString(entry.getValue()).replace("\\[|\\]", "").replaceAll("\\s", ",");
            if (!paramsIndexMapping.containsKey(entry.getKey())) {
                continue;
            }
            int index = paramsIndexMapping.get(entry.getKey());
            paramValues[index] = caseStringValue(value, parameterTypes[index]);
        }

        if (paramsIndexMapping.containsKey(HttpServletRequest.class.getName())) {
            int reqIndex = paramsIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = request;
        }

        if (paramsIndexMapping.containsKey(HttpServletResponse.class.getName())) {
            int respIndex = paramsIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex] = response;
        }

        Object invoke = method.invoke(controller, paramValues);
        if (invoke == null || invoke instanceof Void) {
            return null;
        }

        if (method.getReturnType() == SunModelAndView.class) {
            return (SunModelAndView) invoke;
        }
        return null;
    }

    private Object caseStringValue(String value, Class<?> parameterType) {
        if (String.class.equals(parameterType)) {
            return value;
        } else if (Integer.class.equals(parameterType)) {
            return Integer.valueOf(value);
        } else if (Double.class.equals(parameterType)) {
            return Double.valueOf(value);
        } else if (Boolean.class.equals(parameterType)) {
            return Boolean.valueOf(value);
        } else {
            if (value != null) {
                return value.toString();
            }
            return value;
        }
    }
}
