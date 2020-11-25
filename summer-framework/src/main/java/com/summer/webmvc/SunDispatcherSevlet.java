package com.summer.webmvc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.summer.annotation.Controller;
import com.summer.annotation.RequestMapping;
import com.summer.context.SunApplicationContext;
import com.summer.context.support.SunGenericApplicationContext;
import com.summer.webmvc.annotition.ResponseBody;
import com.summer.webmvc.annotition.RestController;
import com.summer.webmvc.servlet.ContentType;
import com.summer.webmvc.servlet.SunHandlerAdapter;
import com.summer.webmvc.servlet.SunHandlerMapping;
import com.summer.webmvc.servlet.SunModelAndView;
import com.summer.webmvc.servlet.SunView;
import com.summer.webmvc.servlet.SunViewResolver;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.util.StringUtil;

/**
 * 前端控制器
 */
@Slf4j
public class SunDispatcherSevlet extends HttpServlet {

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
    public static final SunApplicationContext applicationContext = SunGenericApplicationContext.getInstance();

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
        assert adapter != null;
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
        if (handlerMapping.getIsResponseBody()) {
            response.setContentType(ContentType.jsonContentType.value);
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(GSON.toJson(mv));
        } else if (SunModelAndView.class.equals(handlerMapping.getMethod().getReturnType())) {
            processDispatchResult(request, response, (SunModelAndView) mv);
        }
    }

    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response, SunModelAndView mv) throws IOException {
        if (null == mv || viewResolver.isEmpty()) {
            return;
        }
        for (SunViewResolver resolver : viewResolver) {
            SunView view = resolver.resolveViewName(mv.getViewName(), null);
            view.render(mv.getModel(), request, response);
            return;
        }
    }

    private SunHandlerAdapter gethandlerAdapter(SunHandlerMapping handler) {
        if (handlerAdapter.isEmpty()) {
            return null;
        }
        SunHandlerAdapter adapter = handlerAdapter.get(handler);
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
        for (SunHandlerMapping handlerMapping : handlerMappings) {
            Matcher matcher = handlerMapping.getPattern().matcher(requestURI);
            if (!matcher.matches()) {
                continue;
            }
            return handlerMapping;
        }
        return null;
    }

    public static void doRefresh() {
        log.info("=============================== init ====================================");
        applicationContext.refresh();
        initStrategies(applicationContext);
        log.info("======================= content init successful! ========================");
    }

    /**
     * Initialize the strategy objects that this servlet uses.
     * <p>May be overridden in subclasses in order to initialize further strategy objects.
     * mvc的9大策略
     */
    protected static void initStrategies(SunApplicationContext context) {
//        多文件上传的组件 todo
        initMultipartResolver(context);
//        初始化本地语言环境 todo
        initLocaleResolver(context);
//        初始化模板处理器 todo
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

    private static void initFlashMapManager(SunApplicationContext context) {
    }

    private static void initViewResolvers(SunApplicationContext context) {
        Properties config = context.getConfig();
        String root = config.getProperty("summer.mvc.template");
        if (StringUtil.isBlank(root)) {
            viewResolver.add(new SunViewResolver("template"));
            return;
        }
        viewResolver.add(new SunViewResolver(root));
    }

    private static void initRequestToViewNameTranslator(SunApplicationContext context) {
    }

    private static void initHandlerExceptionResolvers(SunApplicationContext context) {
    }

    private static void initHandlerAdapters(SunApplicationContext context) {
        for (SunHandlerMapping handlerMapping : handlerMappings) {
            handlerAdapter.put(handlerMapping, new SunHandlerAdapter());
        }
    }

    private static void initHandlerMappings(SunApplicationContext context) {
        String[] definitionNames = context.getBeanDefinitionNames();
        for (String definitionName : definitionNames) {
            Object bean = context.getBean(definitionName);
            Class<?> clazz = bean.getClass();
            if (!clazz.isAnnotationPresent(Controller.class) && !clazz.isAnnotationPresent(RestController.class)) {
                continue;
            }
            //类注解
            String classPath = "";
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping clazzAnnotation = clazz.getAnnotation(RequestMapping.class);
                classPath = clazzAnnotation.value()[0];
            }
            boolean clazzResponseBody = clazz.isAnnotationPresent(ResponseBody.class);
            final boolean restController = clazz.isAnnotationPresent(RestController.class);
            Method[] methods = clazz.getMethods();
            StringBuilder sb = new StringBuilder();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }
                RequestMapping methodMapping = method.getAnnotation(RequestMapping.class);
                String methodPath = methodMapping.value()[0];
                sb.append("/");
                if (Objects.nonNull(classPath)) {
                    sb.append(classPath).append("/");
                }
                sb.append(methodPath);
                String url = sb.toString().replace("*", ".*").replaceAll("/+", "/");
                Pattern compile = Pattern.compile(url);
                SunHandlerMapping handlerMapping = SunHandlerMapping.builder().controller(bean).method(method).pattern(compile).build();
                handlerMapping.setIsResponseBody(true); //默认json返回
                final Class<?> returnType = method.getReturnType();
                if (returnType.getTypeName().equals(SunModelAndView.class.getTypeName())) {
                    handlerMapping.setIsResponseBody(false);
                }
                final boolean methodResponseBody = method.isAnnotationPresent(ResponseBody.class);
                //虽然可能出现返回值是SunModelAndView，但是这样加这三个注解任意一个，依旧按json返回
                if (clazzResponseBody || methodResponseBody || restController) {
                    handlerMapping.setIsResponseBody(true);
                }
                handlerMappings.add(handlerMapping);
                log.info("HandlerMapping ==> {} ", handlerMapping.toString());
                sb.setLength(0);
            }
        }
    }

    private static void initThemeResolver(SunApplicationContext context) {
    }

    private static void initLocaleResolver(SunApplicationContext context) {
    }

    private static void initMultipartResolver(SunApplicationContext context) {
    }
}
