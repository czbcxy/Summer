package com.summer.framework.webmvc;

import com.google.gson.Gson;
import com.summer.framework.annotation.Controller;
import com.summer.framework.annotation.RequestMapping;
import com.summer.framework.context.SunApplicationContext;
import com.summer.framework.context.support.SunGenericApplicationContext;
import com.summer.framework.webmvc.servlet.*;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 前端控制器
 */
@Slf4j
@WebServlet(name = "dispatcherServlet", urlPatterns = {"/"}, loadOnStartup = 1)
public class SunDispatcherSevlet extends HttpServlet {

    private static final String CONTEXT_CONFIG_LOCATION = "Applicaiton.properties";
    private static final Gson gson = new Gson();
    private SunApplicationContext context = null;

    private static final List<SunHandlerMapping> handlerMappings = new ArrayList<SunHandlerMapping>();

    private static final Map<SunHandlerMapping, SunHandlerAdapter> handlerAdapter = new ConcurrentHashMap<>(64);

    private static final List<SunViewResolver> viewResolver = new ArrayList<>();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        this.doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            this.doDispatcher(request, response);
        } catch (IOException e) {
            log.error("IOException log ==> {}", e.getMessage());
        }
    }

    private void doDispatcher(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //先去reqeust中拿到url，匹配handlerMapping
        SunHandlerMapping handlerMapping = getHandler(request);
        if (handlerMapping == null) {
            processDispatchResult(request, response, new SunModelAndView("404"));
            return;
        }
        //准备调用前的参数
        SunHandlerAdapter adapter = gethandlerAdapter(handlerMapping);

        //调用方法,返回值和模板
        Object[] paramValue = adapter.handle(request, response, handlerMapping);
        Object mv = null;
        try {
            mv = adapter.Invoke(handlerMapping.getController(), handlerMapping.getMethod(), paramValue);
        } catch (Exception e) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("errorCode", e.getCause().getMessage());
            model.put("stackTrace", e.getCause().getStackTrace());
            processDispatchResult(request, response, new SunModelAndView("500", model));
        }
        if (SunModelAndView.class.equals(handlerMapping.getMethod().getReturnType())) {
            processDispatchResult(request, response, (SunModelAndView) mv);
        } else {
            response.setContentType(ContentType.jsonContentType.value);
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(gson.toJson(mv));
        }
    }

    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response, SunModelAndView mv) throws IOException {
        if (null == mv || viewResolver.isEmpty()) {
            return;
        }
        for (SunViewResolver resolver : viewResolver) {
            SunErrorView view = resolver.resolveViewName(mv.getViewName(), null);
            view.render(mv.getModel(), request, response);
            return;
        }
    }

    private SunHandlerAdapter gethandlerAdapter(SunHandlerMapping handler) {
        if (this.handlerAdapter.isEmpty()) {
            return null;
        }
        SunHandlerAdapter adapter = this.handlerAdapter.get(handler);
        if (adapter.supports(handler)) {
            return adapter;
        }
        return null;
    }

    private SunHandlerMapping getHandler(HttpServletRequest request) {
        if (handlerMappings.isEmpty()) {
            return null;
        }
        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();
        log.info("context => {} ", contextPath);
        log.info("requestURI => {} ", requestURI);
        for (SunHandlerMapping handlerMapping : this.handlerMappings) {
            try {
                Matcher matcher = handlerMapping.getPattern().matcher(requestURI);
                if (!matcher.matches()) {
                    continue;
                }
                return handlerMapping;
            } catch (Exception e) {
                throw e;
            }
        }
        return null;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        log.info("=============================== init ====================================");
        context = new SunGenericApplicationContext(CONTEXT_CONFIG_LOCATION);
        for (String definitionName : context.getBeanDefinitionNames()) {
            log.info("bean :==> {}", definitionName);
        }
        log.info("size :==> {}", context.getBeanDifinitionCount());
        log.info("======================= content init successful! ========================");
        initStrategies(context);
    }

    /**
     * Initialize the strategy objects that this servlet uses.
     * <p>May be overridden in subclasses in order to initialize further strategy objects.
     * mvc的9大策略
     */
    protected void initStrategies(SunApplicationContext context) {
//        多文件上传的组件 todo
        initMultipartResolver(context);
//        初始化本地语言环境 todo
        initLocaleResolver(context);
//        舒适化模板处理器 todo
        initThemeResolver(context);
//        1，初始化handlerMapping
        initHandlerMappings(context);
//        2，初始化参数适配器
        initHandlerAdapters(context);
//        3，初始化异常拦截器
        initHandlerExceptionResolvers(context);
//        初始化视图预处理器 todo
        initRequestToViewNameTranslator(context);
//        4，初始化视图转换器
        initViewResolvers(context);
//        参数缓存器 todo
        initFlashMapManager(context);
    }

    private void initFlashMapManager(SunApplicationContext context) {
    }

    private void initViewResolvers(SunApplicationContext context) {
        Properties config = context.getConfig();
        String root = config.getProperty("Root");
        this.viewResolver.add(new SunViewResolver(root));
    }

    private void initRequestToViewNameTranslator(SunApplicationContext context) {
    }

    private void initHandlerExceptionResolvers(SunApplicationContext context) {
    }

    private void initHandlerAdapters(SunApplicationContext context) {
        for (SunHandlerMapping handlerMapping : handlerMappings) {
            this.handlerAdapter.put(handlerMapping, new SunHandlerAdapter());
        }
    }

    private void initHandlerMappings(SunApplicationContext context) {
        String[] definitionNames = context.getBeanDefinitionNames();
        for (String definitionName : definitionNames) {
            Object bean = context.getBean(definitionName);
            Class<?> clazz = bean.getClass();
            if (!clazz.isAnnotationPresent(Controller.class)) {
                continue;
            }
            //类注解
            String classPath = "";
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping clazzAnnotation = clazz.getAnnotation(RequestMapping.class);
                classPath = clazzAnnotation.value()[0];
            }

            Method[] methods = clazz.getMethods();
            StringBuffer sb = new StringBuffer();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }
                RequestMapping methodMapping = method.getAnnotation(RequestMapping.class);
                String methodPath = methodMapping.value()[0];
                sb.append("/").append(classPath).append("/").append(methodPath);
                String url = sb.toString().replace("*", ".*").replaceAll("/+", "/");
                Pattern compile = Pattern.compile(url);
                SunHandlerMapping handlerMapping = new SunHandlerMapping().setController(bean).setMethod(method).setPattern(compile);
                this.handlerMappings.add(handlerMapping);
                log.info("HandlerMapping ==> {} ", handlerMapping.toString());
                sb.setLength(0);
            }

        }

    }

    private void initThemeResolver(SunApplicationContext context) {
    }

    private void initLocaleResolver(SunApplicationContext context) {
    }

    private void initMultipartResolver(SunApplicationContext context) {
    }
}
