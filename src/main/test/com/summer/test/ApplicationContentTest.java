package com.summer.test;

import com.summer.example.controller.ControllerBase;
import com.summer.framework.context.support.SunGenericApplicationContext;
import org.junit.jupiter.api.Test;

public class ApplicationContentTest {

    @Test
    public void content() {
        SunGenericApplicationContext context = new SunGenericApplicationContext(new String[]{"Applicaiton.properties"});
        ControllerBase controllerDemo = (ControllerBase) context.getBean("controllerBase");
        System.out.println(controllerDemo.world());
    }
}
